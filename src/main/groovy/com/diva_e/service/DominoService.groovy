package com.diva_e.service

import com.diva_e.data.Domino
import com.diva_e.data.DominoData
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import com.tinkerpop.gremlin.groovy.GremlinGroovyPipeline
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
/**
 * Created by jbinder on 17.07.17.
 */
@Service
class DominoService {

    @Autowired
    private OrientGraph orientGraph

    DominoData getDominosForId(Integer moduleId, List<String> selected) {
        if (selected.size() > 0) {
            return getMatchingDominosForModule(moduleId, selected)
        } else {
            return getAllDominosForModuleId(moduleId)
        }
    }

    DominoData getAllDominosForModuleId(Integer moduleId) {
        DominoData dominoData = new DominoData()
        List<Domino> dominos = new ArrayList<>()
        Iterable<Vertex> vertices = orientGraph.getVertices("Domino.moduleId", moduleId)

        vertices.iterator().forEachRemaining({
            dominos.add(createDominoFromVertex(it))
        })
        dominoData.setDominos(dominos)
        dominoData.setActiveModules(["1", "2", "3", "4", "5"])
        return dominoData
    }


    DominoData getMatchingDominosForModule(Integer moduleId, List<String> selected) {

        Collections.sort(selected)
        String startProductId = selected.get(0)
        selected.remove(startProductId)

        def startPoint = orientGraph.getVertices("dominoId", startProductId).iterator().next()

        GremlinGroovyPipeline pipeIn = new GremlinGroovyPipeline()
        GremlinGroovyPipeline pipeOut = new GremlinGroovyPipeline()
        pipeIn.start(startPoint).as("start").out().loop("start"){it.loops < 6 }
        {
            def list = []; it.path.each{ list.add(it.getProperty('dominoId'))}; list.add(it.object.getProperty('dominoId'));

            if(selected.size() == 0) {
                return true
            } else {
                return list.containsAll(selected)
            }


        }.path{it}

        pipeOut.start(startPoint).as("start")
        pipeOut.in().loop("start")
        {it.loops < 6 }
        {
            def list = []; it.path.each{ list.add(it.getProperty('dominoId'))};
            list.add(it.object.getProperty('dominoId')); return true }
        .path{it }

        return extractResults(startProductId, moduleId, pipeIn, pipeOut)

    }

    private DominoData extractResults(String startProductId, int moduleId, GremlinGroovyPipeline pipeIn, GremlinGroovyPipeline pipeOut) {
        DominoData dominoData = new DominoData()
        HashSet<String> dominos = new HashSet<>()
        HashSet<String> activeModules = new HashSet<>()

        if (startProductId.startsWith(moduleId + "_")) {
            dominos.add(startProductId)
        }

        for (List<List<OrientVertex>> allPathOut : pipeIn.toList()) {
            allPathOut.each {
                activeModules.add(it.getProperty("moduleId"))
                if (it.getProperty("moduleId").equals(String.valueOf(moduleId))) {
                    dominos.add(createDominoFromVertex(it))
                }
            }
        }

        for (List<List<OrientVertex>> allPathIn : pipeOut.toList()) {
            allPathIn.each {
                activeModules.add(it.getProperty("moduleId"))
                if (it.getProperty("moduleId").equals(String.valueOf(moduleId))) {
                    dominos.add(createDominoFromVertex(it))
                }
            }
        }
        dominoData.setDominos(dominos.asList())
        dominoData.setActiveModules(activeModules.asList())
        dominoData
    }



    private Domino createDominoFromVertex(Vertex it) {
        Domino domino = new Domino()
        domino.setDominoId(it.getProperty("dominoId"))
        domino.setModuleId(it.getProperty("moduleId"))
        domino.setColorsIn(it.getProperty("colorsIn"))
        domino.setColorsOut(it.getProperty("colorsOut"))
        domino.setInValue(it.getProperty("inValue"))
        domino.setOutValue(it.getProperty("outValue"))
        domino
    }
}
