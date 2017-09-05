package com.diva_e.data;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by jbinder on 20.05.17.
 */
@Getter
@Slf4j
public class DominoDataCreator {

    @Autowired
    private OrientGraph orientGraph;

    private static final Map<Integer, Map<String, List<String>>> moduleColorsMap = new LinkedHashMap<>();
    private static Iterator<String> colorIterator;
    private static final List<String> colors = Arrays.asList("#91d7fb", "#b1ab5c", "#dc4d79");//, "orange", "grey", "white", "purple");


    private static String getColor() {
        if(!colorIterator.hasNext()) {
            colorIterator = colors.iterator();
        }
        return colorIterator.next();
    }

    private static List<String> getColorList() {
        return Arrays.asList(getColor(), getColor());
    }

    @PostConstruct
    public void createData() {
        colorIterator = colors.iterator();
        for (int i = 1; i <= 5; i++) {
            createVertices(i);
        }
        orientGraph.commit();
        createEdges();
        orientGraph.commit();

    }

    private void createVertices(Integer moduleId) {
        for(int in = 0; in <= 6; in++) {
            createInAndOutDominos(moduleId, in);
        }
    }

    private void createInAndOutDominos(Integer moduleId, Integer in) {
        for(int out = 0; out <= 6; out++) {

            String id = moduleId + "_" + in + "_" + out;
            OrientVertex orientVertex = orientGraph.addVertex(id + ",class:Domino");
            orientVertex.setProperty("dominoId", id);
            orientVertex.setProperty("moduleId", String.valueOf(moduleId));
            orientVertex.setProperty("inValue", in);
            orientVertex.setProperty("outValue", out);
                orientVertex.setProperty("colorsIn", getColorList());
                orientVertex.setProperty("colorsOut", getColorList());
            log.info("Created vertex with id: " + id + " and properties " + orientVertex.getProperties());

        }
    }

    private void createEdges() {
        for(int i = 1; i < 5; i++) {
            //select all vertices for each module and create edges to the descending modules
            orientGraph.getVertices("Domino.moduleId", String.valueOf(i)).iterator().forEachRemaining((Vertex vertex) -> {
                List<String> colorsOutList = (List<String>)vertex.getProperty("colorsOut");
                StringBuilder sql = new StringBuilder();
                sql.append("CREATE EDGE FROM ");
                sql.append(vertex.getId());
                sql.append(" TO (select from Domino where  moduleId > '");
                sql.append(Integer.valueOf(vertex.getProperty("moduleId")));
                sql.append("'");
                sql.append(" AND inValue = " + vertex.getProperty("outValue"));
                sql.append(" AND colorsIn contains '"  + colorsOutList.get(0) + "'");
                sql.append(" AND colorsIn contains '"  + colorsOutList.get(1) + "'");
                sql.append(");");
                try {
                    orientGraph.command(new OCommandSQL(sql.toString())).execute();
                }
                catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            });

        }
    }
}
