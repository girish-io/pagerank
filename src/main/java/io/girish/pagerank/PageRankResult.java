package io.girish.pagerank;

import java.util.HashMap;
import java.util.List;

/*
 * Stores PageRank metrics and computed results
 */
public record PageRankResult(
        HashMap<String, List<Double>> prIterations,
        HashMap<String, Double> finalPageRankValues,
        int iterations,
        int totalNodes,
        double initialDistribution,
        String reasonForComplete
) {}
