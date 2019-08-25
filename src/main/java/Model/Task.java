package Model;
import Model.Node;

/**
 * Represent a Task schedule on a specified processor
 * A Task's attributes : scheduled node, start time, end time, scheduled processor
 * A Task is essentially a wrapper for the nodes
 * Just nodes with a start time
 */
public class Task {
    private Node _node;
    private int _processorID;
    private int _startTime;
    private int _endTime;

    /**
     * Instantiates an instance of Task
     * @param n The node associated with the task
     * @param processor The processor associated with the task
     */
    public Task (Node n, int processor, int startTime) {
        _node = n;
        _processorID = processor;
        _startTime = startTime;
        _endTime = _startTime + _node.getWeight();
    }

    /**
     * Gets the node associated with the task
     * @return Node
     */
    public Node getNode () {
        return _node;
    }

    /**
     * Gets the processor associated with the task
     * @return int
     */
    public int getProcessorID () {
        return _processorID;
    }

    /**
     * Gets the start time of the task
     * @return int
     */
    public int getStartTime(){
        return _startTime;
    }

    /**
     * Gets the end time of the task
     * @return int
     */
    public int getEndTime(){
        return _endTime;
    }

}
