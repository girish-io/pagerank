package io.girish.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DirectedGraph implements Graph {
    private int totalRelationships = 0;

    // We use a LinkedHashMap because we want to preserve the order the nodes were loaded in through graph files
    private final LinkedHashMap<String, Node> nodes = new LinkedHashMap<>();
    private final LinkedHashMap<String, HashMap<String, Node>> nodeOutlinks = new LinkedHashMap<>();
    private final LinkedHashMap<String, HashMap<String, Node>> nodeInlinks = new LinkedHashMap<>();

    @Override
    public Node createNode(String nodeId) {
        Node node = new Node(nodeId);
        this.nodes.put(nodeId, node);

        HashMap<String, Node> outlinks = new HashMap<>();
        this.nodeOutlinks.put(nodeId, outlinks);

        HashMap<String, Node> inlinks = new HashMap<>();
        this.nodeInlinks.put(nodeId, inlinks);

        return this.nodes.get(nodeId);
    }

    @Override
    public Node deleteNode(String nodeId) {
        Node removedNode = this.nodes.remove(nodeId);

        for (String outlinkNodeId : this.nodeOutlinks.get(nodeId).keySet()) {
            this.nodeInlinks.get(outlinkNodeId).remove(nodeId);
            this.nodes.get(outlinkNodeId).setInlinks(this.nodeInlinks.get(outlinkNodeId));
        }

        for (String inlinkNodeId : this.nodeInlinks.get(nodeId).keySet()) {
            this.nodeOutlinks.get(inlinkNodeId).remove(nodeId);
            this.nodes.get(inlinkNodeId).setOutlinks(this.nodeOutlinks.get(inlinkNodeId));
        }

        return removedNode;
    }

    @Override
    public void createLink(String fromNodeId, String toNodeId) {
        Node fromNode = this.nodes.get(fromNodeId);
        Node toNode = this.nodes.get(toNodeId);

        this.nodeOutlinks.get(fromNodeId).put(toNodeId, toNode);
        this.nodeInlinks.get(toNodeId).put(fromNodeId, fromNode);

        fromNode.addOutlink(toNode);
    }

    @Override
    public int getTotalNodes() {
        return this.nodes.size();
    }

    @Override
    public Collection<Node> getNodes() {
        return this.nodes.values();
    }

    @Override
    public Collection<Node> getInlinks(String nodeId) {
        return this.nodeInlinks.get(nodeId).values();
    }

    @Override
    public HashMap<String, HashMap<String, Node>> getNodeOutlinks() {
        return this.nodeOutlinks;
    }

    @Override
    public int getTotalRelationships() {
        for (String nodeId : this.nodeOutlinks.keySet()) {
            totalRelationships += this.nodeOutlinks.get(nodeId).values().size();
        }

        return totalRelationships;
    }
}
