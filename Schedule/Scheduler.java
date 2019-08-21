package Schedule;

import Graph.Graph;
import Input.InputHandler;
import Graph.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class Scheduler {
    private State _rootState;
    private  Graph _graph;
    private PriorityQueue<State> _currentStates = new PriorityQueue<State>();
    private int  _numberOfProcessors;

    /**
     * This method will initialize the current state with numbers of processors and reachable nodes.
     * @param Graph g
     * @param InputHandler i
     */
    public Scheduler(Graph g, InputHandler i){
        this._graph = g;
        this._numberOfProcessors = i.getNumberOfProcessors();
        this._rootState = new State(this._numberOfProcessors,g.getNodes(),g);
        this._rootState.initializeReachableNodes(this._graph);
        this._currentStates.add(this._rootState);
    }

    /**
     * This method calculates a valid schedule
     * @return Valid Schedule
     */
    public State schedule(){
        int boundary;
        while (_currentStates.peek().getscheduledNodes().size() != _graph.getNodes().size()) {
            for (State s : this._currentStates){
                s.refreshReachableNodes();
            }
            this._currentStates =  this.nextState();
        }

        State boundaryState = this._currentStates.poll();
        boundary = boundaryState.getCost();
        Iterator<State> iter = this._currentStates.iterator();

        while (iter.hasNext()) {
            State str = iter.next();
            if (str.getEstimatedCost() == boundary && str.getscheduledNodes().size() < boundaryState.getscheduledNodes().size()){
                //schedule();
            }else {
                iter.remove();
            }
        }
        return boundaryState;
    }

    /**
     * This method will return next valid states based on the current state
     * @return Next valid state
     */
    public PriorityQueue<State> nextState() {
       // System.out.println(_currentStates.peek().getEstimatedCost());
        //System.out.println("----------");
        for (State s: _currentStates){
            //System.out.println(s.getEstimatedCost());
        }
        //System.out.println("----------");

        List<State> states = this._currentStates.poll().getAllPossibleNextStates(this._graph);
        for (State s :states){
            this._currentStates.add(s);
        }
        return this._currentStates;
    }
}
