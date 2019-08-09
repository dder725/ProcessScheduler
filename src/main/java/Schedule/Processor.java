package Schedule;

import Graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Processor {
    private final int _id;
    private int _endTime;
    private List<Task> _scheduledTasks = new ArrayList<Task>();
    private List<Node> _scheduledNodes = new ArrayList<Node>();

    HashMap<Node,Task> nodeWithTask = new HashMap<Node, Task>();

    public Processor(int i){
        this._id = i;
    }

    /**
     * Get the end_Time for the specified processor
     * @return _endTime
     */
    public int getEndTime(){
        return _endTime;
    }

    public List<Task> get_scheduledTasks(){
        return _scheduledTasks;
    }

    public void schedule(Node nextNodeToSchedule, int startTime) {
        Task newTask = new Task(nextNodeToSchedule, _id, startTime);
        _scheduledTasks.add(newTask);
        _scheduledNodes.add(nextNodeToSchedule);

    }

    public List<Node> getAllNodes(){
        return _scheduledNodes;
    }

    public List<Task> getAllTasks(){
        return _scheduledTasks;
    }

}
