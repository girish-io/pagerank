package io.girish.graph;

import java.util.Collection;
import java.util.HashMap;

public interface Graph {
    Node createNode(String nodeId);
    Node deleteNode(String nodeId);

    int getTotalNodes();
    Collection<Node> getNodes();

    Collection<Node> getInlinks(String nodeId);
    HashMap<String, HashMap<String, Node>> getNodeOutlinks();
    void createLink(String fromNodeId, String toNodeId);

    int getTotalRelationships();
}
