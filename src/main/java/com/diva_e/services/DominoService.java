package com.diva_e.services;

import com.diva_e.data.Domino;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbinder on 28.06.17.
 */
@Service
public class DominoService {

    @Autowired
    private OrientGraph orientGraph;

    public List<Domino> getDominosForId(Integer moduleId) {

        List<Domino> dominos = new ArrayList<>();
        Iterable<Vertex> vertices = orientGraph.getVertices("Domino.moduleId", moduleId);
        vertices.iterator().forEachRemaining(vertex -> {
            Domino domino = new Domino();
            domino.setDominoId(vertex.getProperty("dominoId"));
            domino.setModuleId(vertex.getProperty("moduleId"));
            domino.setColorsIn(vertex.getProperty("colorsIn"));
            domino.setColorsOut(vertex.getProperty("colorsOut"));
            domino.setInValue(vertex.getProperty("inValue"));
            domino.setOutValue(vertex.getProperty("outValue"));
            dominos.add(domino);
        });
        return dominos;

    }
}
