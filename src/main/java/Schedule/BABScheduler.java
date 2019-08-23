//package Schedule;
//
//import Graph.*;
//import Input.InputHandler;
//import java.util.Stack;
//
///**
// * This is a branch and bound scheduler
// */
//public class BABScheduler implements Scheduler {
//    private Graph _g;
//    private InputHandler _i;
//    private Stack<State> _states = new Stack<>();
//    private int _bound=Integer.MAX_VALUE;
//    private boolean firstTimeComplete = true;
//    private State _currentFinalState;
//    public BABScheduler(Graph g, InputHandler i){
//        this._g = g;
//        this._i = i;
//    }
//
//    @Override
//    public State schedule() {
//        State initialState = new State(_i.getNumberOfProcessors(), _g.getNodes(),_g);
//        _states.push(initialState);
//        while(!_states.isEmpty()) {
//            while(!_states.isEmpty()&&this.checkForStateCompleteness(_states.peek())){                                      //if it is a complete state pop it
//                if (firstTimeComplete) {
//                    initialiseTheBound(_states.pop());
//                    firstTimeComplete = false;
//                }else if(this.checkForStateCompleteness(_states.peek())&&_bound>_states.peek().getCost()){//update bound if the bound is bigger than the Completestate cost
//                    this._currentFinalState = _states.pop();
//                    this._bound = this._currentFinalState.getCost();
//                }else{
//                    _states.pop();
//                }
//                this.removeStateHaveBiggerCostThanTheBound();
//            }
//
//            if(!_states.isEmpty()) {
//                State parent = _states.pop();
//                parent.refreshReachableNodes();
//                //for every child state push them into the stack
//                for (int j = 0; j < _i.getNumberOfProcessors(); j++) {                  //for every processor
//                    for (Node n : parent.getReachableNodes()) {                                         //for every nodes
//                        //make a new state
//                        State newState = new State(parent, n, j);
//                        this._states.push(newState);
//                    }
//                }
//            }else{
//                return _currentFinalState;
//            }
//        }
//
//
//
//
//
//
//        return this._currentFinalState;
//    }
//
//    private void removeStateHaveBiggerCostThanTheBound() {
//        while(!_states.isEmpty()&&_states.peek().getCost()>=this._bound){
//            _states.pop();
//        }
//    }
//
//
//    /**
//     * this method is only called once when the first complete state has been reached
//     * @param firstCompleteState
//     */
//    public void initialiseTheBound(State firstCompleteState){
//        this._bound = firstCompleteState.getCost();
//    }
//
//    /**
//     * return true if the input state is complete
//     * @param state
//     * @return
//     */
//    public Boolean checkForStateCompleteness(State state){
//        return state.getscheduledNodes().size()==this._g.getNodes().size();
//    }
//
//}
