import com.orientechnologies.orient.core.sql.OCommandSQL
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.impls.orient.OrientVertex
import com.tinkerpop.gremlin.groovy.GremlinGroovyPipeline
import spock.lang.Specification
/**
 * Created by jbinder
 */
class JugSaxonyExamples extends Specification {


    def "1 - api example - create a verticle"() {

        when:
        OrientGraph graph = new OrientGraph("memory:jugsaxony")

        OrientVertex vertex = graph.addVertex("1")
        vertex.setProperty("name", "jochen")

        then:
        graph.getVertices().each {
            it.getProperty("name")
        }

    }


    def "2 - api example - create a edge between two verticles"() {

        when:
        OrientGraph graph = new OrientGraph("memory:jugsaxony")

        OrientVertex jochen = graph.addVertex("1")
        jochen.setProperty("name", "jochen")

        OrientVertex michael = graph.addVertex("2")
        michael.setProperty("name", "michael")

        jochen.addEdge("knows", michael)


        then:
        graph.getEdges().each {
            println it
        }
     }



    def "3 - create first sql query"() {

        when:
        OrientGraph graph = new OrientGraph("memory:jugsaxony")

        OrientVertex jochen = graph.addVertex("1")
        jochen.setProperty("name", "jochen")

        OrientVertex michael = graph.addVertex("2")
        michael.setProperty("name", "michael")

        jochen.addEdge("knows", michael)


        then:
        String sql = "select * from V"
        graph.command(new OCommandSQL(sql)).execute().each {
            println it
        }


    }


    def "4 - create some gremlin action"() {

        OrientGraph graph = new OrientGraph("memory:domino", false)

        when:
        def jochen = graph.addVertex("1")
        jochen.setProperty("name", "Jochen")

        def martin = graph.addVertex("2")
        martin.setProperty("name", "Martin")

        def marie = graph.addVertex("3")
        marie.setProperty("name", "Marie")

        def linda = graph.addVertex("4")
        linda.setProperty("name", "Linda")


        graph.addEdge("11", jochen, martin, "knows")
        marie.addEdge("works with", martin)
        marie.addEdge("friend of", linda)

        graph.commit()

        then:

        GremlinGroovyPipeline pipeline = new GremlinGroovyPipeline()
        def out = pipeline.start(jochen).out("knows").in("works with").out().path({it.getProperty("name")})

        out.toList().each {
            println it
        }


    }

}
