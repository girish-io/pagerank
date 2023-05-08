package io.girish.repositories;

import io.girish.Config;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

public class GraphRepository {
    private final String NODE_TYPE = "Website";

    private final Driver driver = GraphDatabase.driver(Config.NEO4J_URI, AuthTokens.basic(Config.NEO4J_USERNAME, Config.NEO4J_PASSWORD));
    private final Session session = driver.session();

    public void createNode(String nodeId) {
        this.session.run("""
                CREATE (%s:%s {name:"%s"}) RETURN %s""".formatted(nodeId, this.NODE_TYPE, nodeId, nodeId));
    }

    public void createRelationship(String fromNodeId, String toNodeId) {
        session.run("""
                MATCH (a:%s), (b:%s) WHERE a.name='%s' AND b.name='%s' CREATE (a)-[r:LINKS_TO]->(b) RETURN type(r)""".formatted(
                        this.NODE_TYPE, this.NODE_TYPE, fromNodeId, toNodeId));
    }

    public void updatePageRank(String nodeId, double newPageRank) {
        session.run("""
                    MATCH (n:%s {name:"%s"}) SET n.pagerank=%f RETURN n""".formatted(this.NODE_TYPE, nodeId, newPageRank));
    }

    /*
     * Delete all existing nodes from our database
     */
    public void deleteAllNodes() {
        session.run("MATCH (n) CALL { WITH n DETACH DELETE n } IN TRANSACTIONS");
    }
}
