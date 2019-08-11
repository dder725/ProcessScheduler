import Schedule.State;
import Schedule.Processor;
import Graph.Node;
import Graph.Dependency;
import Graph.Graph;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.util.*;
public class StateTest {
    private State currentState1;
    private State currentState2;
    private State currentState3;
    private State state;
    private Node node1;
    private Node node2;
    private Node node3;
    private Node node4;
    private List<Node> ln=new ArrayList<Node>();
    private Dependency dependency1;
    private Dependency dependency2;
    private Dependency dependency3;
    private Dependency dependency4;
    private List<Dependency> ld1 = new ArrayList<Dependency>();
    private List<Dependency> ld2 = new ArrayList<Dependency>();
    private List<Dependency> ld3 = new ArrayList<Dependency>();
    private List<Dependency> ld4 = new ArrayList<Dependency>();
    private Processor processor1;
    private Processor processor2;
    private Map<Node, List<Dependency>> edges = new HashMap<Node, List<Dependency>>();
    private State initialState;
    private Graph g = new Graph("g",ln,edges);
    @org.junit.jupiter.api.BeforeEach

    @Before
    void setUp() throws FileNotFoundException {
        node1 = new Node("A",2);
        node2 = new Node("B",2);
        node3 = new Node("C",3);
        node4 = new Node("D",2);
        ln.add(node1);
        ln.add(node2);
        ln.add(node3);
        ln.add(node4);
        dependency1 = new Dependency(node1,node2,2);
        dependency2 = new Dependency(node1,node3,1);
        dependency3 = new Dependency(node2,node4,2);
        dependency4 = new Dependency(node3,node4,1);
        ld1.add(dependency1);
        ld1.add(dependency2);
        ld2.add(dependency3);
        ld3.add(dependency4);
        edges.put(node1,ld1);
        edges.put(node2,ld2);
        edges.put(node3,ld3);
        edges.put(node4,ld4);
        processor1 = new Processor(1);
        processor2 = new Processor(2);
        initialState=new State(2,null,g);
    }

    @Test
    void testReachableNodes() {
        assertEquals(true,initialState.existReachablenodes());
    }

    @Test
    void testInitialStartTime() {
        assertEquals(0,initialState.calculateTaskStartTime(1,node1));
    }

    @Test
    void testStartTime() {
        currentState1=new State (initialState,node1,1);
        assertEquals(2,currentState1.calculateTaskStartTime(1,node1));
    }



}