package Output;

import Graph.*;
import Schedule.*;
import java.util.*;

public class OutputHandler {



    private List<Processor> processorList = new ArrayList<Processor>();
    private TaskToDotFile toDotFile = new TaskToDotFile();
    private Graph _graph;
    private State finalState;
    private String finalOutput = "";

    public OutputHandler(){ }

    public OutputHandler(State state){
        finalState = state;
        processorList = finalState.getProcessors();
        _graph = state.getGraph();

    }


    private void getTasks(){
        List<Task> tasks = new ArrayList<Task>();
        // sort all tasks by its start time
        for(Processor p : processorList){
            tasks.addAll(p.getAllTasks());
            Collections.sort(tasks, new Comparator<Task>(){
                public int compare(Task t1, Task t2){
                    if(t1.getStartTime() <= t2.getStartTime()){
                        return 1;
                    }else{
                        return 0;
                    }
                }
            });

        }

        for( Task t: tasks){
            finalOutput = finalOutput+toDotFile.setTask(t);
            finalOutput = finalOutput+findDpendency(t.getNode());

        }

    }

    private String findDpendency(Node child){
        String dpString = "";
        // Find all dependencies of child node
        List<Dependency> dList = _graph.getLinkEdges().get(child);
        for(Dependency d : dList){
            // ensure the child node is the child of that dependency
            if(d.getChild().getName().equals(child.getName())){
                String dp = d.getParent().getName()+" -> "+child.getName()+"      [Weight = "+String.valueOf(d.getWeight())+"];\n";
                dpString = dpString + dp;
            }
        }
        return dpString;

    }

    public String getFinalOutput() {
        getTasks();
        return finalOutput;
    }
}
