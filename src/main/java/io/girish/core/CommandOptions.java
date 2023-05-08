package io.girish.core;

import com.beust.jcommander.Parameter;

public class CommandOptions {
    @Parameter(description = "<graph-file>", required = true)
    public String graphFile;

    @Parameter(names = {"-h", "--help"}, description = "Show help usage and exit", help = true, order = 6)
    public boolean help;

    @Parameter(names = {"-v", "--verbose"}, description = "Verbose output while computing PageRank iterations", order = 5)
    public boolean verbose = false;

    @Parameter(names = {"-i", "--iterations"}, description = "Number of PageRank iterations (Set to 0 to iterate till convergence)", order = 0)
    public int pageRankIterations = 5;

    @Parameter(names = {"-d", "--damping"}, description = "Damping factor for PageRank", order = 2)
    public double pageRankDampingFactor = 0.85;

    @Parameter(names = {"-e", "--epsilon"}, description = "Epsilon for convergence (only used when iterations have been set to 0)", order = 1)
    public double convergenceEpsilon = 0.0001;

    @Parameter(names = {"-c", "--config"}, description = "Configuration file to use for database connection", order = 4)
    public String configFilePath = "./pagerank.conf";

    @Parameter(names = {"--reset-db"}, description = "Whether to delete all nodes and relations in the graph database.", order = 3)
    public boolean resetDb = false;
}
