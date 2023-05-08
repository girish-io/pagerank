package io.girish.graph;

import java.util.Collection;
import java.util.HashMap;

public class Node {
    String nodeId;

    private HashMap<String, Node> inlinks = new HashMap<>();
    private HashMap<String, Node> outlinks = new HashMap<>();

    protected Node(String nodeId) {
        this.nodeId = nodeId;
    }

    protected void addOutlink(Node node) {
        this.outlinks.put(node.getNodeId(), node);
    }

    protected void setInlinks(HashMap<String, Node> inlinks) {
        this.inlinks = inlinks;
    }

    protected void setOutlinks(HashMap<String, Node> outlinks) {
        this.outlinks = outlinks;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    @SuppressWarnings("unused")
    public Collection<Node> getInlinks() {
        return this.inlinks.values();
    }

    public int getTotalOutlinks() {
        return this.outlinks.size();
    }

    @Override
    public String toString() {
        return "<Node id=\"" + this.nodeId + "\" outlinks=" + outlinks + ">";
    }
}
