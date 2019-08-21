package Graph;

import java.util.*;

/**
 * This class represents a node/task in a graph
 */
public class Node {
    private final String name;
    private final int weight;
    private List<Node> parents;
    private List<Node> children;
    private int parentsCount;
    private boolean isSourceNode = true;
    private int bottomLevel;

    public Node(String name, int weight){
        this.name = name;
        this.weight = weight;
        parentsCount = 0;
        parents = new ArrayList<Node>();
        children = new ArrayList<Node>();
    }

    public void setBottomLevel() {
        this.bottomLevel = calculateBottomLevel(this);

    }

    public void addParent(Node parent){
        if(isSourceNode == true){
            isSourceNode = false;
        }
        if(!parents.contains(parent)) {
            parents.add(parent);
        }
        updateParentsCount(true);
    }

    public void addChild(Node child){
        if(!parents.contains(child)) {
            children.add(child);
        }
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Node> getParents(){
        return parents;
    }

    public boolean isSourceNode(){
        return isSourceNode;
    }

    public String getName(){
        return this.name;
    }

    public int getWeight() { return this.weight; }

    public void updateParentsCount(boolean toIncrease){
        if(toIncrease){
            parentsCount+=1;
        } else {
            parentsCount -= 1;
        }
    }

    public int getParentsCount(){
        return parentsCount;
    }

    @Override
    public boolean equals(Object obj) {
        Node node2 = (Node)obj;
        return (this.name == node2.getName());
    }

    /**
     * A Node is reachable if and only if all of its parents are in the scheduled list of the currentState
     * @return
     */
    public boolean isReachable(List<Node> scheduledList){

        if(scheduledList.containsAll(this.getParents())){
            return true;
        }else if(isSourceNode){
        	return true;
        }else {
        	return false;
        }
        
    }

    public int calculateBottomLevel(Node n) {
        int currentMax = 0;
        if(n.getChildren().size()==0){
            currentMax = n.getWeight();
        }
        for (Node c : n.getChildren()) {
            int bottom = n.getWeight() + calculateBottomLevel(c);
            if (bottom > currentMax) { // if a task has multiple children, take the shortest path from them
                currentMax = bottom;
            }
        }
        return currentMax;
    }

    public int getBottomLevel() {
        return this.bottomLevel;
    }
}
