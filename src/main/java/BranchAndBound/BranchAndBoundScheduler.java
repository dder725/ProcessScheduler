package BranchAndBound;

import Graph.Graph;
import Input.InputHandler;
import Schedule.State;

public class BranchAndBoundScheduler extends Algorithm{
    private State _currentState;
    private  Graph _graph;
    private int  _numberOfProcessors;
    public BranchAndBoundScheduler(Graph g, InputHandler i){
        this._graph = g;
        this._numberOfProcessors = i.getNumberOfProcessors();
        this._currentState = new State(this._numberOfProcessors,g.getNodes(),g);
        this._currentState.initializeReachableNodes(this._graph);
    }

}
