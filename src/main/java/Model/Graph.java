package Model;

import java.util.*;

/**
 * This class represents a Graph that holds all the nodes and dependencies.
 * and only one graph instatance is allowed in the program
 */
public class Graph {
    private final List<Node> nodes;
    private final String gName; //name of the graph
    private final Map<Node, List<Dependency>> edges;
    private int minimumProcessingCost;
    private List<Node> sortedNodes;
    private HashMap<String, Integer> bottomLevels;

    public Graph(String name, List<Node> nodes,Map<Node,List<Dependency>> edges) {
        this.gName = name;
        this.nodes = nodes;
        this.edges = edges;
        for (Node node: nodes) {
            minimumProcessingCost += node.getWeight();
            node.setBottomLevel();
//            System.out.println("Node "+node.getName()+"'s bottom level is: "+node.getBottomLevel());
        }

        this.bottomLevels=calculateBottomLevel();
    }

    /**
     * Get the minimum cost of processing of all tasks excluding the cost of process transfer
     */
    public int getMinimumCost(){
        return minimumProcessingCost;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Source nodes are the nodes which doesn't have a parent
     * @return
     */
    public List<Node> getSourceNodes() {
        List<Node> sourceNodes = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.isSourceNode()) {
                sourceNodes.add(node);
            }
        }
        return sourceNodes;
    }

  //TODO Is this function necessary or the nodes are always returned in topological order?
   private void topologicalOrder(){
       sortedNodes = new LinkedList<Node>();
       List<Node> noIncomingEdgeNodes = new LinkedList<Node>();

       for (Node node : nodes){
           if(node.isSourceNode()){
                noIncomingEdgeNodes.add(node);
           }
       }

       while(!noIncomingEdgeNodes.isEmpty()){
           for(Iterator<Node> iterator = noIncomingEdgeNodes.iterator(); iterator.hasNext();){
               Node sourceNode = noIncomingEdgeNodes.iterator().next();
               noIncomingEdgeNodes.remove(sourceNode);
               sortedNodes.add(sourceNode);
               if(edges.containsKey(sourceNode)) {
                   for (Dependency dependency : edges.get(sourceNode)) {
                       dependency.getChild().updateParentsCount(false);
                       //If a child node has no more parents, it can be added to a source node list
                       if (dependency.getChild().getParentsCount() == 0) {
                           noIncomingEdgeNodes.add(dependency.getChild());
                       }
                   }
               }
           }
       }
   }

   public List<Node> getSortedNodes(){
       topologicalOrder();
       return sortedNodes;
   }

   public Map<Node, List<Dependency>> getLinkEdges(){
        return edges;
   }

    /**
     * Calculate bottom level for all nodes in digraph
     * @return HashMap<String, Integer> A map of bottom level for each node
     */
    private HashMap<String, Integer> calculateBottomLevel() {
        HashMap<String, Integer> bottomLevels = new HashMap<String, Integer>();

        sortedNodes = getSortedNodes();
        for (int i = sortedNodes.size() - 1; i >= 0; i--) {
            Node n = sortedNodes.get(i);
            int bottomLevel = 0;
            for (Node childNode : n.getChildren()) {
                bottomLevel = Math.max(bottomLevel, bottomLevels.get(childNode.getName()) + childNode.getWeight());
            }
            bottomLevels.put(n.getName(), bottomLevel);
        }
        return bottomLevels;
    }

    public int getBottomLevel(Node node){
        //if (bottomLevels.)
        return bottomLevels.get(node.getName());
    }

    public HashMap<String, Integer> getBottomLevels(){
        return bottomLevels;
    }


}
