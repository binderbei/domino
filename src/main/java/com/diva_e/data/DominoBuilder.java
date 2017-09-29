package com.diva_e.data;

import com.tinkerpop.blueprints.Vertex;

/**
 * Created by jbinder on 24.09.17.
 */
public class DominoBuilder {

    public Domino build(Vertex vertex) {
            Domino domino = new Domino();
            domino.setDominoId(vertex.getProperty("dominoId"));
            domino.setModuleId(vertex.getProperty("moduleId"));
            domino.setColorsIn(vertex.getProperty("colorsIn"));
            domino.setColorsOut(vertex.getProperty("colorsOut"));
            domino.setInValue(vertex.getProperty("inValue"));
            domino.setOutValue(vertex.getProperty("outValue"));
            return domino;
    }
}
