import io.girish.core.Logger;
import io.girish.graph.DirectedGraph;
import io.girish.graph.Graph;
import io.girish.graph.GraphFile;
import io.girish.graph.Node;
import io.girish.pagerank.PageRank;
import io.girish.pagerank.PageRankResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPageRank {
    @Test
    @DisplayName("PR(A)=0.12")
    void prOfAShouldBePoint12() {
        Logger.setVerbose(false);

        Graph graph = new DirectedGraph();

        // Create and initialize our nodes
        graph.createNode("A");
        graph.createNode("B");
        graph.createNode("C");
        graph.createNode("D");

        graph.createLink("A", "B");
        graph.createLink("A", "C");

        graph.createLink("B", "D");

        graph.createLink("C", "A");
        graph.createLink("C", "D");
        graph.createLink("C", "B");

        graph.createLink("D", "C");

        double dampingFactor = 1;
        double convergenceEpsilon = 0.0001;
        int iterations = 0;  // 0 means iterate till convergence

        PageRank pr = new PageRank(graph);
        PageRankResult pageRankResult = pr.compute(iterations, convergenceEpsilon, dampingFactor);

        assertEquals(0.12495981224279835, pageRankResult.finalPageRankValues().get("A"));
        assertEquals(15, pageRankResult.iterations());
    }

    @Test
    @DisplayName("Load graph from file")
    void graphShouldLoadFromFile() {
        String graphLocation = "./src/test/res/network.graph";
        Graph graphFile = GraphFile.read(graphLocation);

        int expectedNodes = 12;
        int expectedRelationships = 38;

        assertEquals(expectedNodes, graphFile.getTotalNodes());
        assertEquals(expectedRelationships, graphFile.getTotalRelationships());
    }

    @Test
    @DisplayName("Node should be deleted from graph")
    void nodeShouldBeDeletedFromGraph() {
        Logger.setVerbose(false);

        Graph graph = new DirectedGraph();

        // Create and initialize our nodes
        graph.createNode("A");
        graph.createNode("B");
        graph.createNode("C");
        graph.createNode("D");

        graph.createLink("A", "B");
        graph.createLink("A", "C");

        graph.createLink("B", "D");

        graph.createLink("C", "A");
        graph.createLink("C", "D");
        graph.createLink("C", "B");

        graph.createLink("D", "C");

        int expectedTotalNodesAfterDelete = 3;
        String expectedDeletedNode = "A";

        Node deletedNode = graph.deleteNode(expectedDeletedNode);

        assertEquals(expectedTotalNodesAfterDelete, graph.getTotalNodes());
        assertEquals(expectedDeletedNode, deletedNode.getNodeId());
    }

    @Test
    @DisplayName("Node should be created in graph")
    void nodeShouldBeCreatedInGraph() {
        Logger.setVerbose(false);

        Graph graph = new DirectedGraph();

        // Create and initialize our nodes
        Node createdNode = graph.createNode("A");

        graph.createNode("B");
        graph.createNode("C");
        graph.createNode("D");

        graph.createLink("A", "B");
        graph.createLink("A", "C");

        graph.createLink("B", "D");

        graph.createLink("C", "A");
        graph.createLink("C", "D");
        graph.createLink("C", "B");

        graph.createLink("D", "C");

        int expectedTotalNodesAfterCreation = 4;
        String expectedCreatedNode = "A";

        assertEquals(expectedTotalNodesAfterCreation, graph.getTotalNodes());
        assertEquals(expectedCreatedNode, createdNode.getNodeId());
    }

    @Test
    @DisplayName("Node should have two inlinks")
    void nodeShouldHaveTwoInlinks() {
        Logger.setVerbose(false);

        Graph graph = new DirectedGraph();

        // Create and initialize our nodes
        graph.createNode("A");
        graph.createNode("B");
        graph.createNode("C");
        graph.createNode("D");

        graph.createLink("A", "B");
        graph.createLink("A", "C");

        graph.createLink("B", "D");

        graph.createLink("C", "A");
        graph.createLink("C", "D");
        graph.createLink("C", "B");

        graph.createLink("D", "C");

        int expectedTotalInlinks = 2;

        assertEquals(expectedTotalInlinks, graph.getInlinks("C").size());
    }
}
