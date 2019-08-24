package CostFunction;

import Model.*;
import Input.InputHandler;
import Model.State;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class DFSsearch {
    private State _currentState;
    private  Graph _graph;
    private int  _numberOfProcessors;
    private List<State> completeStates= new ArrayList<State>();
    private List<Node> nodesVisited= new ArrayList<Node>();
    private PriorityQueue<State> compeletStates = new PriorityQueue<State>();

    public DFSsearch(Graph g, InputHandler i){
        this._graph = g;
        this._numberOfProcessors = i.getNumberOfProcessors();
        this._currentState = new State(this._numberOfProcessors,g.getNodes(),g);
        this._currentState.initializeReachableNodes(this._graph);
    }
   // public State produceSchedule(State initialState) {

        //return final_state;
    
   // }


}
