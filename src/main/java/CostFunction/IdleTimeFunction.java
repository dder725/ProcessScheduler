package CostFunction;

import Model.*;
import Model.State;
import Model.Task;

public class IdleTimeFunction {
    private int numProcessors;
    private int taskWeightSum;
    private Graph _g;
    public int getNumProcessors() {
        return numProcessors;
    }

    /**
     * Constructor that constructs the fields in cost function
     */

    public IdleTimeFunction(int numProcessors, Graph g) {
        this.numProcessors = numProcessors;
        this._g=g;
        this.taskWeightSum = 0;
    }


    public int calculate(State s) {
        //calculate the weight sum of all nodes
        for (Node n:s.getGraph().getNodes())
        {
            this.taskWeightSum += n.getWeight();
        }

        int idleTime = 0; // stores the idle time across all processors
        for (int i = 0; i < numProcessors; i++) {
            int expectedStartTime = 0;

            // sum up idle time of each processor that has scheduled tasks on it
            if (s.getProcessors().get(i).getAllTasks() != null) {
                for (Task st : s.getProcessors().get(i).getAllTasks()) {
                    // add to idleTime whenever an empty slot is detected between tasks
                    int actualStartTime = st.getStartTime();
                    idleTime += actualStartTime - expectedStartTime;

                    // the next task can start exactly when the current task finishes
                    expectedStartTime = st.getStartTime() + st.getNode().getWeight();
                }
            }

        }

        return (taskWeightSum + idleTime) / numProcessors;
    }
}
