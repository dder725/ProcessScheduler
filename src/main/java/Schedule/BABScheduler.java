package Schedule;

import Model.*;
import Input.InputHandler;
import java.util.Stack;

/**
 * This is a branch and bound scheduler
 */
public class BABScheduler extends Algorithm {
    private Graph _g;
    private InputHandler _i;
    private Stack<State> _states = new Stack<>();
    private int _bound=Integer.MAX_VALUE;
    private boolean firstTimeComplete = true;
    private State _currentFinalState;
    private RuntimeMonitor _runtimeMonitor;

    public BABScheduler(Graph g, InputHandler i){
        this._g = g;
        this._i = i;
        this._runtimeMonitor = RuntimeMonitor.getInstance();


    }


    @Override
    public State schedule() {
        State initialState = new State(_i.getNumberOfProcessors(), _g.getNodes(),_g);
        if(_runtimeMonitor != null) {
            _runtimeMonitor.start(_i.getNumberOfProcessors(), Integer.parseInt(_i.getNumberOfCores()), initialState); // Start the monitor
        }
        _states.push(initialState);
        while(!_states.isEmpty()) {
            while(!_states.isEmpty()&&this.checkForStateCompleteness(_states.peek())){                                      //if it is a complete state pop it
                if(_runtimeMonitor != null) {
                    _runtimeMonitor.updateOptimal(_states.peek()); // Update the monitor
                }
                if (firstTimeComplete) {
                    initialiseTheBound(_states.pop());
                    firstTimeComplete = false;
                }else if(this.checkForStateCompleteness(_states.peek())&&_bound>_states.peek().getCost()){//update bound if the bound is bigger than the Completestate cost
                    this._currentFinalState = _states.pop();
                    this._bound = this._currentFinalState.getCost();
                }else{
                    _states.pop();
                }
                this.removeStateHaveBiggerCostThanTheBound();
            }

            if(!_states.isEmpty()) {
                State parent = _states.pop();
                parent.refreshReachableNodes();

                //for every child state push them into the stack
                for (int j = 0; j < _i.getNumberOfProcessors(); j++) {                  //for every processor
                    for (Node n : parent.getReachableNodes()) {                                         //for every nodes
                        //make a new state
                        State newState = new State(parent, n, j);

                        if(_runtimeMonitor != null) {
                            _runtimeMonitor.incrementStatesExplored();  //Update the monitor
                        }

                        this._states.push(newState);
                    }
                }
            }else{
                if(_runtimeMonitor != null) {
                    _runtimeMonitor.finish(_currentFinalState);
                }
                return _currentFinalState;
            }
        }





        if(_runtimeMonitor != null) {
            _runtimeMonitor.finish(_currentFinalState);
        }
        return this._currentFinalState;
    }

    private void removeStateHaveBiggerCostThanTheBound() {
        while(!_states.isEmpty()&&_states.peek().getCost()>=this._bound){
            _states.pop();
        }
    }


    /**
     * this method is only called once when the first complete state has been reached
     * @param firstCompleteState
     */
    public void initialiseTheBound(State firstCompleteState){
        this._bound = firstCompleteState.getCost();
    }

    /**
     * return true if the input state is complete
     * @param state
     * @return
     */
    public Boolean checkForStateCompleteness(State state){
        return state.getscheduledNodes().size()==this._g.getNodes().size();
    }

}
