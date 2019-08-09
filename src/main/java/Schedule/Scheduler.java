package Schedule;

import Graph.Graph;
import Input.InputHandler;
import Graph.Node;
import Graph.Dependency;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Scheduler {
    private State _currentState;
    private  Graph _graph;
    private int  _numberOfProcessors;
    //private List<Task> allTasks = new ArrayList<Task>();

    /**
     * This method will initialize the current state with numbers of processors and reachable nodes.
     * @param Graph g
     * @param InputHandler i
     */
    public Scheduler(Graph g, InputHandler i){
        _graph = g;
        _numberOfProcessors = i.getNumberOfProcessors();
        this._currentState = new State(_numberOfProcessors);
        this._currentState.initializeReachableNodes(_graph);
        this._currentState.initialSchedule();
    }

    /**
     * This method will return next valid state based on the current state
     * @return Next valid state
     */
    public State nextState() {
        List<State> states = _currentState.getAllPossibleNextStates(this._graph);
        State nextState = this.getTheBestCostState(states);
        return nextState;
    }

    private State getTheBestCostState(List<State> states) {
        for (int i = 0; i< states.size();i++){

        }
        return null;
    }

    /**
     * This method calculates a valid schedule
     * @return Valid Schedule
     */
    public State schedule(){
        while (!_currentState.existReachablenodes()){

            State nextState =  this.nextState();
            //

            nextState.refreshReachableNodes();
            this._currentState =  nextState;
        }
        return this._currentState;
    }

/*    public Map<Node, List<Dependency>> getLinkEdges(){
        return _graph.getLinkEdges();
    }*/




}
