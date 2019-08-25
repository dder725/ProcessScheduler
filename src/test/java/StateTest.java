import CostFunction.BottomLevelFunction;
import Model.State;
import Schedule.Processor;
import Model.Node;
import Model.Dependency;
import Model.Graph;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.util.*;

public class StateTest {
    private State currentState1;
    private State currentState2;
    private State currentState3;
    private State currentState4;
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
    private BottomLevelFunction btf=new BottomLevelFunction();
    @org.junit.jupiter.api.BeforeEach

    @Before
    void setUp() throws FileNotFoundException {
        node1 = new Node("A",2);
        node2 = new Node("B",2);
        node3 = new Node("C",3);
        node4 = new Node("D",2);
        node1.addChild(node2);
        node1.addChild(node3);
        node2.addChild(node4);
        node3.addChild(node4);
        node2.addParent(node1);
        node3.addParent(node1);
        node4.addParent(node3);
        node4.addParent(node2);
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
        processor1 = new Processor(0);
        processor2 = new Processor(1);
        initialState=new State(2,null,g);
    }
    /**
     * Test the existence of  reachable nodes
     */
    @Test
    void testReachableNodes() {
        assertEquals(false,initialState.existReachablenodes());
    }

    /**
     * Test the start time of the initial state
     */
    @Test
    void testInitialStartTime() {
        assertEquals(0,initialState.calculateTaskStartTime(1,node1));
    }

    /**
     * Test the start time of the second state
     */
    @Test
    void testStartTime() {
        currentState1=new State (initialState,node1,1);
        assertEquals(2,currentState1.calculateTaskStartTime(1,node1));
    }

    /**
     * Test the start time of any existing state
     */
    @Test
    void testCurrentStartTime(){
        currentState1=new State (initialState,node1,0);
        currentState2=new State (currentState1,node2,0);
        currentState3=new State (currentState2,node3,1);
        assertEquals(6,currentState3.calculateTaskStartTime(1,node4));
    }

    /**
     * Test the Bottom level of the nodes
     */
    @Test
    void testBottomLevel(){
        assertEquals(7,node1.calculateBottomLevel(node1));
        assertEquals(4,node1.calculateBottomLevel(node2));
        assertEquals(5,node1.calculateBottomLevel(node3));
    }

    /**
     * Test the Bottom level of the States
     */
    @Test
    void testBottomLevelState(){
        currentState1=new State (initialState,node1,1);
        currentState2=new State (currentState1,node2,1);
        assertEquals(7,btf.calculateBottom(currentState2));
    }

    /**
     * Test the estimated cost of existing state
     */
    @Test
    void testEstimateCost1(){
        currentState1=new State (initialState,node1,0);
        currentState2=new State (currentState1,node2,0);
        currentState3=new State (currentState2,node3,1);
        assertEquals(7,currentState1.getEstimatedCost());
    }

    @Test
    void testEstimateCost2(){
        currentState1=new State (initialState,node1,0);
        currentState2=new State (currentState1,node2,0);
        currentState3=new State (currentState2,node3,1);
        assertEquals(7,currentState2.getEstimatedCost());
    }

    @Test
    void ttestEstimateCost3(){
        currentState1=new State (initialState,node1,0);
        currentState2=new State (currentState1,node2,0);
        currentState3=new State (currentState2,node3,1);
        assertEquals(8,currentState3.getEstimatedCost());
    }
}