import Input.InputHandler;
import Model.Graph;
import Model.State;
import Parser.DotFileParser;
import Schedule.AStarScheduler;
import Schedule.Algorithm;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    private DotFileParser fileParser;
    private static String TEST_FILE_NAME;
    private Graph graph;
    private InputHandler input;

    @Before
    public void setUp() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_10_Random.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
    }


    @Test
    public void testCost10for2processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_10_Random.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "2";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(50, finalState.getCost());
    }

    @Test
    public void testCost7for2processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_7_OutTree.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "2";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(28, finalState.getCost());
    }

    @Test
    public void testCost8for2processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_8_Random.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "2";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(581, finalState.getCost());
    }

    @Test
    public void testCost9for2processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_9_SeriesParallel.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "2";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(55, finalState.getCost());
    }

    @Test
    public void testCost11for2processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_11_OutTree.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "2";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(350, finalState.getCost());
    }

    @Test
    public void testCost10for4processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_10_Random.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "4";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(50, finalState.getCost());
    }

    @Test
    public void testCost7for4processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_7_OutTree.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "4";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(22, finalState.getCost());
    }

    @Test
    public void testCost8for4processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_8_Random.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "4";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(581, finalState.getCost());
    }

    @Test
    public void testCost9for4processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_9_SeriesParallel.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "4";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(55, finalState.getCost());
    }

    @Test
    public void testCost11for4processors() throws FileNotFoundException {
        File inputFile = new File(this.getClass().getResource("/Nodes_11_OutTree.dot").getFile());
        TEST_FILE_NAME = inputFile.getAbsolutePath();
        fileParser = new DotFileParser();
        graph = fileParser.parseDotFile(TEST_FILE_NAME);
        String[] args = new String[2];
        args[0] = TEST_FILE_NAME;
        args[1] = "4";
        input = new InputHandler(args);
        Algorithm algo = new AStarScheduler(graph, input);
        State finalState = algo.schedule();
        assertEquals(227, finalState.getCost());
    }
}