package Output;

import Schedule.Task;


public class TaskToDotFile {
    private Task _task;
    private String format;

    public TaskToDotFile(){
    }

    public String setTask(Task task){
        _task=task;
        getFormat();
        if(format==null){
            return "";
        }else{
            return format;
        }


    }

    private void getFormat(){
        String nodeName = _task.getNode().getName();
        String processerID = String.valueOf(_task.getProcessorID ());
        String startTime = String.valueOf(_task.getStartTime ());
        String weight = String.valueOf(_task.getNode().getWeight ());
        format = _task.getNode().getName()+"           [Weight="+weight+",Start="+startTime+",Processor="+processerID+"];\n";



    }
}
