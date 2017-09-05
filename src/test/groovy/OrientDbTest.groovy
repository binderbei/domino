

import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.gremlin.groovy.GremlinGroovyPipeline
import spock.lang.Specification
/**
 * Created by jbinder
 */
class OrientDbTest extends Specification {


    def "first creation of a verticle"() {


        OrientGraph graph = new OrientGraph("memory:domino")

        when:
        def jochen = graph.addVertex("1")
        jochen.setProperty("name", "Jochen")
        def thomas = graph.addVertex("2")
        thomas.setProperty("name", "Thomas")
        graph.commit()

        then:
        graph.getVertices().each {
            println it.getProperty("name")
        }

    }

    def "first creation of an edge"() {
        OrientGraph graph = new OrientGraph("memory:domino")

        when:
        def jochen = graph.addVertex("1")
        jochen.setProperty("name", "Jochen")
        def thomas = graph.addVertex("2")
        thomas.setProperty("name", "Thomas")

        jochen.addEdge("knows", thomas)
        graph.commit()

        then:

        graph.getEdges().each {
            println it
        }
    }

    def "Create first sql"() {

        OrientGraph graph = new OrientGraph("memory:domino")

        when:
        def jochen = graph.addVertex("1")
        jochen.setProperty("name", "Jochen")
        def thomas = graph.addVertex("2")
        thomas.setProperty("name", "Thomas")
        graph.commit()

        String sql = "select * from V"

        then:
        def result = graph.command(new OCommandSQL(sql)).execute()

        result.each {
            println it.getProperty("name")
        }

    }


    def "create first gremlin action !"() {

        OrientGraph graph = new OrientGraph("memory:domino", false)

        when:
        def jochen = graph.addVertex("1")
        jochen.setProperty("name", "Jochen")
        def thomas = graph.addVertex("2")
        thomas.setProperty("name", "Thomas")
        def michael = graph.addVertex("3")
        michael.setProperty("name", "Michael")
        def tobias = graph.addVertex("4")
        tobias.setProperty("name", "Tobias")


        graph.addEdge("11", jochen, thomas, "knows")
        thomas.addEdge("knows", michael)
        michael.addEdge("knows", tobias)

        graph.commit()

        then:

        GremlinGroovyPipeline pipeline1 = new GremlinGroovyPipeline()
        GremlinGroovyPipeline pipeline2= new GremlinGroovyPipeline()


        def result = pipeline1.start(jochen).as("start").out().out()
        result.toList().each {println it.getProperty("name")}

        def result2 = pipeline2.start(jochen).as("start").out().loop("start"){it.loops < 4}{ return true}.path({it.getProperty("name")})
        result2.toList().each {println it}

    }

}
