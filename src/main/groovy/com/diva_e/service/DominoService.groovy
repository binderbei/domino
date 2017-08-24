package com.diva_e.service

import com.diva_e.data.Domino
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

    List<Domino> getDominosForId(Integer moduleId, List<String> selected) {

        if (selected.size() > 0) {
            return getMatchingDominosForModule(moduleId, selected)
        } else {
            return getAllDominosForModuleId(moduleId)
        }
    }

    List<Domino> getAllDominosForModuleId(Integer moduleId) {
        List<Domino> dominos = new ArrayList<>()
        Iterable<Vertex> vertices = orientGraph.getVertices("Domino.moduleId", moduleId)

        vertices.iterator().forEachRemaining({
            dominos.add(createDominoFromVertex(it))
        })
        return dominos
    }


    List<Domino> getMatchingDominosForModule(Integer moduleId, List<String> selected) {

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

        HashSet<String> result = extractResults(startProductId, moduleId, pipeIn, pipeOut)

        return result.asList()

    }

    private HashSet<String> extractResults(String startProductId, int moduleId, GremlinGroovyPipeline pipeIn, GremlinGroovyPipeline pipeOut) {
        HashSet<String> result = new HashSet<>()

        if (startProductId.startsWith(moduleId + "_")) {
            result.add(startProductId)
        }

        for (List<List<OrientVertex>> allPathOut : pipeIn.toList()) {
            allPathOut.each {
                if (it.getProperty("moduleId").equals(String.valueOf(moduleId))) {
                    result.add(createDominoFromVertex(it))
                }
            }
        }

        for (List<List<OrientVertex>> allPathIn : pipeOut.toList()) {
            allPathIn.each {
                if (it.getProperty("moduleId").equals(String.valueOf(moduleId))) {
                    result.add(createDominoFromVertex(it))
                }
            }
        }
        result
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
