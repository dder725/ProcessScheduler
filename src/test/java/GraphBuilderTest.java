import Graph.Graph;
import Graph.Node;
import Parser.DotFileParser;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphBuilderTest {
    private DotFileParser fileParser;
    private static final String TEST_FILE_NAME = "/home/twelve_koalas/IdeaProjects/Project1/src/main/resources/Nodes_10_Random.dot";
    private File file;
    private Graph graph;

    @org.junit.jupiter.api.BeforeEach

    @Before
    void setUp() throws FileNotFoundException {
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
    }

    /**
     * Test that the graph contains all nodes
     */
    @org.junit.jupiter.api.Test
    void testAllNodes() {
        assertEquals(10, graph.getNodes().size());
    }

    /**
     * Test that the graph contains all edges
     */
    @org.junit.jupiter.api.Test
    public void testContainsAllEdges() {
        Collection<Node> nodes= graph.getNodes();
        int inEdges = 0;
        int outEdges = 0;

        for (Node node: nodes) {
            inEdges += node.getParents().size();
            outEdges += node.getChildren().size();
        }

        assertEquals(inEdges, outEdges);
        assertEquals(inEdges, 19);
    }

    /**
     * Test that the source nodes are detected correctly
     */
    @Test
    public void testSourceNodes(){
        List<Node> sourceNodes = graph.getSourceNodes();
        assertEquals(sourceNodes.size(), 2);
        assertEquals(sourceNodes.get(0).getName(), "0");
        assertEquals(sourceNodes.get(1).getName(), "1");
    }

    /**
     * Test that corectness of the minimum total cost of the set of tasks executed sequentually
     */
    @Test
    public void testSequentialTime(){
        assertEquals(graph.getMinimumCost(),63);
    }

    /**
     * Check corectness of topological sort
     */
    @Test
    public void getTopologicalSort(){
        Collection<Node> nodes = graph.getNodes();
        List<String> sortedNodes = new LinkedList<String>();
        for(Node node: nodes){
            sortedNodes.add(node.getName());
        }
        List<String> actualList = new LinkedList<String>(Arrays.asList("0","1","2", "3", "4", "5", "6", "7", "8", "9"));
        assertEquals(sortedNodes,actualList);
    }
}