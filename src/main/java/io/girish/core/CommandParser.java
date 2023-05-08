package io.girish.core;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import io.girish.Config;
import io.girish.graph.Graph;
import io.girish.graph.GraphFile;
import io.girish.pagerank.PageRank;
import io.girish.pagerank.PageRankResult;
import io.girish.repositories.GraphRepository;
import io.girish.services.GraphService;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CommandParser {
    public CommandOptions commandOptions = new CommandOptions();

    public void run(String[] args) {
        JCommander jct = JCommander.newBuilder()
                .addObject(commandOptions)
                .build();

        jct.setProgramName("pagerank");

        String error = null;

        try {
            jct.parse(args);
        } catch (ParameterException e) {
            error = e.getMessage();
        }

        if (commandOptions.help) {
            jct.usage();
            System.exit(0);
        }

        if (error != null) {
            jct.usage();
            System.out.println(error);
            System.exit(1);
        }

        // Load configuration from file
        Config.initConfig(commandOptions.configFilePath);
        Logger.notice("Loaded configuration from file: " + commandOptions.configFilePath);

        LocalDateTime startTime = LocalDateTime.now();

        // Start the actual PageRank computation
        PageRankResult pageRankResult = startPageRankComputation();

        LocalDateTime endTime = LocalDateTime.now();

        // Calculate time elapsed for PageRank computation
        final double SECONDS_TO_MILLIS = 1000d;
        double totalTime = ChronoUnit.MILLIS.between(startTime, endTime) / SECONDS_TO_MILLIS;

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);

        // Finished
        Logger.done("PageRank successfully completed in " + pageRankResult.iterations() +
                        " iterations. Took " + df.format(totalTime) + "s.");
    }

    public PageRankResult startPageRankComputation() {
        // Initialize graph repositories
        GraphRepository graphRepository = new GraphRepository();

        // Initialize graph services
        GraphService graphService = new GraphService(graphRepository);

        // Computation verbosity for PageRank
        Logger.setVerbose(commandOptions.verbose);

        if (commandOptions.verbose) {
            Logger.notice("Verbose mode enabled.");
        }

        // Create and initialize our nodes from a graph file
        Logger.info("Loading graph from file: " + commandOptions.graphFile);
        Graph graphFile = GraphFile.read(commandOptions.graphFile);
        Logger.taskFinished("Loaded " + graphFile.getTotalNodes() + " nodes and " +
                                graphFile.getTotalRelationships() + " relationships from file.");

        PageRank pr = new PageRank(graphFile);

        Logger.info("Iterations=" + commandOptions.pageRankIterations + ", " +
                    "DampingFactor=" + commandOptions.pageRankDampingFactor + ", " +
                    "ConvergenceEpsilon=" + commandOptions.convergenceEpsilon);

        if (commandOptions.pageRankIterations == 0) {
            Logger.info("PageRank set to auto-converge.");
        }

        Logger.notice("Started PageRank...");

        PageRankResult pageRankResult = pr.compute(
                commandOptions.pageRankIterations, commandOptions.convergenceEpsilon, commandOptions.pageRankDampingFactor);

        Logger.taskFinished("Completed: " + pageRankResult.reasonForComplete());

        if (commandOptions.resetDb) {
            Logger.info("Resetting graph database...");
            graphService.deleteAllNodes();
        }

        // Creates all nodes and relationships in our graph database
        Logger.info("Creating nodes and relationships in database...");
        graphService.createPageRankGraph(graphFile, pageRankResult);

        Logger.taskFinished("Done.");

        return pageRankResult;
    }
}
