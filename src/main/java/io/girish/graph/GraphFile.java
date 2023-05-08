package io.girish.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class GraphFile {
    private final static int FIRST_LINE = 0;
    private final static int NODE_ID_IDX = 0;
    private final static int NODE_REL_IDX = 1;

    public static Graph read(String fileLocation) {
        BufferedReader reader;
        Graph graph = new DirectedGraph();

        try {
            reader = new BufferedReader(new FileReader(fileLocation));
            String line = reader.readLine();

            for (int lineNumber = 0; line != null; lineNumber++) {
                line = line.replaceAll("\\s{2,}", " ").trim();

                List<String> parsedLine = List.of(line.split(" "));

                if (lineNumber == FIRST_LINE) {
                    // Initialize and create all nodes on our graph
                    for (String nodeId : parsedLine) {
                        graph.createNode(nodeId);
                    }
                } else {
                    String nodeId = parsedLine.get(NODE_ID_IDX);

                    List<String> relationships = parsedLine.subList(NODE_REL_IDX, parsedLine.size());

                    int currentNode = 0;
                    for (Node relatedNode : graph.getNodes()) {
                        String relationship = relationships.get(currentNode);

                        if (isRelationship(relationship) && !nodeId.equals(relatedNode.getNodeId())) {
                            // Create relationship between nodes
                            graph.createLink(nodeId, relatedNode.getNodeId());
                        }
                        currentNode++;
                    }
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return graph;
    }

    private static boolean isRelationship(String relationship) {
        return relationship.equals("1");
    }
}
