package com.diva_e.service

import com.diva_e.data.DominoBuilder
import com.diva_e.data.DominoData
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import com.tinkerpop.gremlin.groovy.GremlinGroovyPipeline

/**
 * Created by jbinder on 24.09.17.
 */
class DominoDataExtractor {

    private DominoBuilder dominoBuilder = new DominoBuilder()


    DominoData extractResults(String startProductId, int moduleId, GremlinGroovyPipeline pipeIn, GremlinGroovyPipeline pipeOut) {
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
                    dominos.add(dominoBuilder.build(it))
                }
            }
        }

        for (List<List<OrientVertex>> allPathIn : pipeOut.toList()) {
            allPathIn.each {
                activeModules.add(it.getProperty("moduleId"))
                if (it.getProperty("moduleId").equals(String.valueOf(moduleId))) {
                    dominos.add(dominoBuilder.build(it))
                }
            }
        }
        dominoData.setDominos(dominos.asList())
        dominoData.setActiveModules(activeModules.asList())
        return dominoData
    }
}
