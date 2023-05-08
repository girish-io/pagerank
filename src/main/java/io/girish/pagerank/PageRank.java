package io.girish.pagerank;

import io.girish.core.Logger;
import io.girish.graph.Graph;
import io.girish.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class PageRank {

    final double MAX_DISTRIBUTION = 1.0;

    private final Graph graph;
    private final double initialDistribution;

    LinkedHashMap<String, List<Double>> prIterations = new LinkedHashMap<>();
    LinkedHashMap<String, List<Double>> prAvgChanges = new LinkedHashMap<>();

    public PageRank(Graph graph) {
        this.graph = graph;
        this.initialDistribution = this.MAX_DISTRIBUTION / this.graph.getTotalNodes();

        for (Node node : this.graph.getNodes()) {
            List<Double> iterations = new ArrayList<>();
            iterations.add(this.initialDistribution);

            this.prIterations.put(node.getNodeId(), iterations);

            this.prAvgChanges.put(node.getNodeId(), new ArrayList<>());
        }
    }

    /*
     * Checks whether PageRank values have converged towards an epsilon.
     */
    public boolean isConverged(double epsilon) {
        return computeAvgPrChange() <= epsilon;
    }

    /*
     * Computes the average change in PageRank values throughout iterations.
     */
    private double computeAvgPrChange() {
        final int MIN_ELEMENTS_FOR_AVG = 2;

        double totalAvg = 0;
        List<Double> allNodeAvgChange = new ArrayList<>();

        for (List<Double> pr : this.prIterations.values()) {
            if (pr.size() >= MIN_ELEMENTS_FOR_AVG) {
                double avgChange = Math.abs(pr.get(pr.size() - 1) - pr.get(pr.size() - 2));
                allNodeAvgChange.add(avgChange);
            }
        }

        for (double avg : allNodeAvgChange) {
            totalAvg += avg;
        }

        int totalNodes = allNodeAvgChange.size();

        return totalAvg / totalNodes;
    }

    /*
     * Creates the final PageRank values after all iterations have completed.
     */
    public HashMap<String, Double> createFinalPageRankValues() {
        HashMap<String, Double> finalPageRanks = new HashMap<>();
        for (String nodeId : this.prIterations.keySet()) {
            List<Double> prLog = this.prIterations.get(nodeId);
            double finalPageRank = prLog.get(prLog.size() - 1);
            finalPageRanks.put(nodeId, finalPageRank);
        }
        return finalPageRanks;
    }

    /*
     * Computes PageRank from a graph for <i> iterations.
     * Set iterations to 0 to compute till the PageRank converges to <e>.
     */
    public PageRankResult compute(int iterations, Double epsilon, double dampingFactor) {
        if (epsilon == null) {
            epsilon = 0.0;
        }

        boolean iterateTillConvergence = iterations == 0;

        int currentIteration = 0;

        while ((iterateTillConvergence && !isConverged(epsilon)) ||
                (!iterateTillConvergence && currentIteration != iterations)) {

            Logger.info("Iteration #" + currentIteration + ", Avg change: " + computeAvgPrChange());

            for (Node node : this.graph.getNodes()) {
                double pr = 0;
                double prPrevious;
                for (Node inNode : this.graph.getInlinks(node.getNodeId())) {
                    if (currentIteration == 0) {
                        prPrevious = this.prIterations.get(inNode.getNodeId()).get(0);
                    } else {
                        prPrevious = this.prIterations.get(inNode.getNodeId()).get(currentIteration);
                    }

                    int outlinks = inNode.getTotalOutlinks();

                    pr += prPrevious / outlinks;
                }

                pr = ((1 - dampingFactor) / graph.getTotalNodes()) + (dampingFactor * pr);

                this.prIterations.get(node.getNodeId()).add(pr);
            }

            currentIteration++;
        }

        String reasonForComplete;
        if (iterateTillConvergence && isConverged(epsilon)) {
            reasonForComplete = "reached convergence: target_epsilon=" + epsilon + ", reached=" + computeAvgPrChange();
        } else {
            reasonForComplete = "reached iteration limit: " + iterations;
        }

        HashMap<String, Double> finalPageRankValues = createFinalPageRankValues();
        return new PageRankResult(
                this.prIterations,
                finalPageRankValues,
                currentIteration,
                this.graph.getTotalNodes(),
                this.initialDistribution,
                reasonForComplete);
    }
}
