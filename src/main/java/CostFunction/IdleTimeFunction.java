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
     * Creates a new IdleTimeFunction instance. This cost function implementation needs to know
     * the number of processors that tasks are scheduled on.
     * @param numProcessors The number of processors that tasks are scheduled on
     */

    public IdleTimeFunction(int numProcessors, Graph g) {
        this.numProcessors = numProcessors;
        this._g=g;
        // calculate sum of task weights at construction time so it is not re-calculated at every call of calculate()
        this.taskWeightSum = 0;

    }


    public int calculate(State s) {
        for (Node n:s.getGraph().getNodes())
        {

            this.taskWeightSum += n.getWeight();
        }
        int idleTime = 0; // stores the idle time across all processors
        for (int i = 0; i < numProcessors; i++) {
            int expectedStartTime = 0; // time that next task should start if no idle time

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
