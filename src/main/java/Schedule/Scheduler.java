package Schedule;

import Graph.Graph;
import Input.InputHandler;

import java.util.List;

public class Scheduler {
    private State _currentState;
    private  Graph _graph;
    private int  _numberOfProcessors;
    /**
     * This method will initialize the current state with numbers of processors and reachable nodes.
     * @param Graph g
     * @param InputHandler i
     */
    public Scheduler(Graph g, InputHandler i){
        this._graph = g;
        this._numberOfProcessors = i.getNumberOfProcessors();
        this._currentState = new State(this._numberOfProcessors,g.getNodes(),g);
        this._currentState.initializeReachableNodes(this._graph);
    }

    /**
     * This method calculates a valid schedule
     * @return Valid Schedule
     */
    public State schedule(){
        while (this._currentState.existReachablenodes()){
            State nextState =  this.nextState();
            this._currentState =  nextState;
            this._currentState.refreshReachableNodes();
        }
        return this._currentState;
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

    /**
     * This method gets the minimum cost state from a list of states
     * @param states
     * @return minimum cost state
     */
    private State getTheBestCostState(List<State> states) {
        int theEvaluatedCost = Integer.MAX_VALUE;
        int index = 0;

        for (int i = 0; i< states.size();i++){
            Heuristic heuristic = new Heuristic(states.get(i));
            if (states.get(i).getCost() + heuristic.getCost() < theEvaluatedCost){
                theEvaluatedCost = states.get(i).getCost() + heuristic.getCost();
                index = i;
            }
        }
        System.out.println(theEvaluatedCost);
        return states.get(index);
    }
}
