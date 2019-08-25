package Model;

/**
 * This class describes relationship between a parent node and a child node
 */
public class Dependency {
    private final Node parent;
    private final Node child;
    private final int weight;

    public Dependency(Node parent, Node child, int weight){
        this.parent = parent;
        this.child = child;
        this.weight = weight;
    }


    public Node getParent(){
        return parent;
    }

    public Node getChild(){
        return child;
    }



    @Override
    public boolean equals(Object obj) {
        Dependency dependency = (Dependency) obj;
        return (dependency.parent == ((Dependency) obj).parent && dependency.child == ((Dependency) obj).child);
    }

    public int getWeight() {
        return weight;
    }
}
