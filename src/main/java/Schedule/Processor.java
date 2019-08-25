package Schedule;

import Model.Node;
import Model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a processor
 */
public class Processor {
    private final int _id;
    private int _endTime;
    private List<Task> _scheduledTasks = new ArrayList<Task>();
    private List<Node> _scheduledNodes = new ArrayList<Node>();

    /**
     * This method will create a new processor with specified id
     * @param i
     */
    public Processor(int i){
        this._id = i;
    }

    /**
     * This method will schedule a node with startTime in a processor and create a new task
     * which is added in a list of tasks named scheduledTask.
     * @param nextNodeToSchedule
     * @param startTime
     */
    public void schedule(Node nextNodeToSchedule, int startTime) {
        Task newTask = new Task(nextNodeToSchedule, _id, startTime);
        _scheduledTasks.add(newTask);
        _scheduledNodes.add(nextNodeToSchedule);
        _endTime = newTask.getEndTime();
    }

    /**
     * Get the end_Time for the specified processor
     * @return _endTime
     */
    public int getEndTime(){
        return _endTime;
    }

    /**
     * This method will return all scheduled nodes in this processor
     * @return _scheduledNodes
     */
    public List<Node> getAllNodes(){
        return _scheduledNodes;
    }

    /**
     * This method will return all scheduled tasks in this processor
     * @return
     */
    public List<Task> getAllTasks(){
        return _scheduledTasks;
    }

    /**
     * return the id of this processor
     * @return
     */
    public int getId(){
        return _id;
    }
}
