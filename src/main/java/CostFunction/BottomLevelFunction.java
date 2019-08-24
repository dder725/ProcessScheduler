package CostFunction;

import Model.State;
import Model.Task;

public class BottomLevelFunction {
    public static int calculateBottom(State state){
        int costFunc = 0;
        for (Task tk : state.getAllTasks()) {
            int minimalTimeToExit = tk.getStartTime()+ tk.getNode().calculateBottomLevel(tk.getNode());
            if (minimalTimeToExit > costFunc) {
                costFunc =  minimalTimeToExit;
            }
        }
        return costFunc;
    }
}
