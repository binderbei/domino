package com.diva_e.service

import com.diva_e.data.Domino
import com.diva_e.data.DominoBuilder
import com.diva_e.data.DominoData
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.orient.OrientGraph
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
    private DominoBuilder dominoBuilder = new DominoBuilder()

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
            dominos.add(dominoBuilder.build(it))
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

        GremlinGroovyPipeline pipeOutgoing = createPipelineFromStartToOutgoingDirection(startPoint, selected)
        GremlinGroovyPipeline pipeIncoming = createPipelineFromStartToIncomingDirection(startPoint)

        return new DominoDataExtractor().extractResults(startProductId, moduleId, pipeOutgoing, pipeIncoming)

    }

    /**
     * We begin with the module of the lowest moduleId. So this is the startpoint of our route through the graph
     * We do not need to check the results if the lower moduleIds are on the path -> every vertex we will find is matching.
     * */
    private GremlinGroovyPipeline createPipelineFromStartToIncomingDirection(Vertex startPoint) {
        GremlinGroovyPipeline pipeIncoming = new GremlinGroovyPipeline()
        pipeIncoming.start(startPoint).as("start")
        pipeIncoming.in().loop("start")
        { it.loops < 6 }
        {
            def listOfActualPathElements = []
            it.path.each { listOfActualPathElements.add(it.getProperty('dominoId')) }
            listOfActualPathElements.add(it.object.getProperty('dominoId'));
            return true
        }
        .path { it }
        pipeIncoming
    }

    /**
     * In outgoing direction to the end of the graph we have to check if the selected vertices are on the path.
     * If yes - the vertex is a correct match
     *
     * */
    private GremlinGroovyPipeline createPipelineFromStartToOutgoingDirection(Vertex startPoint, List<String> selected) {
        GremlinGroovyPipeline pipeOutgoing = new GremlinGroovyPipeline()

        pipeOutgoing.start(startPoint).as("start").out().loop("start")
        { it.loops < 6 }
        {
            def listOfActualPathElements = [];
            it.path.each { listOfActualPathElements.add(it.getProperty('dominoId')) }
            listOfActualPathElements.add(it.object.getProperty('dominoId'))

            if (selected.size() == 0) {
                return true
            } else {
                return listOfActualPathElements.containsAll(selected)
            }


        }.path { it }
        pipeOutgoing
    }
}
