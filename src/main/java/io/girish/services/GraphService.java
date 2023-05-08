package io.girish.services;

import io.girish.graph.Graph;
import io.girish.graph.Node;
import io.girish.pagerank.PageRankResult;
import io.girish.repositories.GraphRepository;

import java.util.HashMap;

public class GraphService {
    private final GraphRepository graphRepository;

    public GraphService(GraphRepository graphRepository) {
        this.graphRepository = graphRepository;
    }

    public void createPageRankGraph(Graph graph, PageRankResult pageRankResult) {
        // Create nodes in database
        for (Node node : graph.getNodes()) {
            String nodeId = node.getNodeId();
            this.graphRepository.createNode(nodeId);
        }

        // Create relationships between created nodes
        for (String nodeId : graph.getNodeOutlinks().keySet()) {
            HashMap<String, Node> outlink = graph.getNodeOutlinks().get(nodeId);
            for (Node outNode : outlink.values()) {
                this.graphRepository.createRelationship(nodeId, outNode.getNodeId());
            }
        }

        // Store pagerank values inside corresponding nodes
        for (String nodeId : pageRankResult.finalPageRankValues().keySet()) {
            double pr = pageRankResult.finalPageRankValues().get(nodeId);

            this.graphRepository.updatePageRank(nodeId, pr);
        }
    }

    public void deleteAllNodes() {
        this.graphRepository.deleteAllNodes();
    }
}
