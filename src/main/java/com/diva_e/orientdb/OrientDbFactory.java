package com.diva_e.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jbinder on 12.04.16.
 */
public class OrientDbFactory {

    private static Logger log = LoggerFactory.getLogger(OrientDbFactory.class);

    public OrientGraph createOrientDataBase(String dbName) {

        OrientGraph graph = new OrientGraph(dbName, false);
        try {
            OrientVertexType product = graph.createVertexType("Domino");
            product.createProperty("dominoId", OType.STRING);
            product.createProperty("moduleId", OType.STRING);
            createIndex(graph);
            graph.setAutoStartTx(true);

        } catch (Exception e) {
            log.error("Error initializing orientdb");
        }
        return graph;
    }

    private void createIndex(OrientGraph graph) {
        String sql = "create index Domino.id on Domino (dominoId) unique";
        OCommandSQL createIndex = new OCommandSQL(sql);
        graph.command(createIndex).execute();
        log.info("Index created from OrientDB");
    }

}


