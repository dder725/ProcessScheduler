package Graph;

import java.util.*;

/**
 * This class is dedicated to building a graph of the problem space
 */
public class GraphBuilder {
    private Graph graph;
    private String name;
    private Map<String, Node> nodes;
    private Map<Node, List<Dependency>> edges; //each parent node has a list of dependencies associated with it

    public GraphBuilder(String name) {
        this.name = name;
        nodes = new HashMap<String, Node>();
        edges = new HashMap<Node, List<Dependency>>();
    }

    /**
     * Add a node to the graph
     *
     * @param label  name of the node
     * @param weight weight of the node
     */
    public void addNode(String label, int weight) {
        Node node = new Node(label, weight);
        nodes.put(label, node);
    }

    /**
     * Add an edge to the graph
     *
     * @param parentLabel
     * @param childLabel
     * @param weight
     */
    public void addDependency(String parentLabel, String childLabel, int weight) {
        Node parentNode = nodes.get(parentLabel);
        Node childNode = nodes.get(childLabel);

        Dependency dependency = new Dependency(parentNode, nodes.get(childLabel), weight);
        if (edges.containsKey(parentNode)) {
            edges.get(parentNode).add(dependency);
            //Add reference to each other for parent/child nodes
            parentNode.addChild(nodes.get(childLabel));
            childNode.addParent(parentNode);
        }
        else if (!edges.containsKey(parentNode)) { //Add dependency to the list
            List<Dependency> dependencies = new ArrayList<Dependency>();
            edges.put(parentNode, dependencies);
            edges.get(parentNode).add(dependency);
            parentNode.addChild(nodes.get(childLabel));
            childNode.addParent(parentNode);
        }
        }

    public Graph createGraph(){
        List<Node> linkedNodes = new LinkedList<Node>();
        for(Node node: nodes.values()){
            linkedNodes.add(node);
        }
        graph = new Graph(name, linkedNodes, edges);
        return graph;
    }
}
