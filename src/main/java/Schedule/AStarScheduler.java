package Schedule;

import Model.Graph;
import Input.InputHandler;
import Model.Node;
import Model.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class AStarScheduler extends Algorithm{
    private State _rootState;
    private  Graph _graph;
    private PriorityQueue<State> _currentStates = new PriorityQueue<State>();
    private int  _numberOfProcessors;
    private int _numberOfCores=0;
    private RuntimeMonitor _runtimeMonitor;

    /**
     * This method will initialize the current state with numbers of processors and reachable nodes.
     */
    public AStarScheduler(Graph g, InputHandler i){
        this._graph = g;
        this._numberOfProcessors = i.getNumberOfProcessors();
        this._rootState = new State(this._numberOfProcessors,g.getNodes(),g);
        this._rootState.initializeReachableNodes(this._graph);
        this._currentStates.add(this._rootState);
        this._numberOfCores = Integer.parseInt(i.getNumberOfCores());
        this._runtimeMonitor = RuntimeMonitor.getInstance();

        if(_runtimeMonitor != null) {
            _runtimeMonitor.start(_numberOfProcessors, _numberOfCores, _rootState);
        }
        //super(g,i);

    }

    /**
     * This method calculates a valid schedule
     * @return Valid Schedule
     */
    @Override
    public State schedule(){
        int boundary;
        int counter = 0;
        while (_currentStates.peek().getscheduledNodes().size() != _graph.getNodes().size()) {
            counter++;
            System.out.println("====counter:"+counter+"=====");
            for (State s : this._currentStates){
                s.refreshReachableNodes();
            }
            StringBuilder strbuilder = new StringBuilder();
            for(Node n:_currentStates.peek().getscheduledNodes()){
                strbuilder.append(n.getName());

            }
            this._currentStates =  this.nextState();
            ExecutorService executorPool1 = Executors.newFixedThreadPool(_numberOfCores);
            Runnable task1 = () -> {
                removeUnnecessaryStates();
            };
            executorPool1.submit(task1);
            executorPool1.shutdown();
            try {
                executorPool1.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
            }
        }

        State boundaryState = this._currentStates.poll();
        boundary = boundaryState.getEstimatedCost();

        Iterator<State> iter = this._currentStates.iterator();
        while (iter.hasNext()) {
            State str = iter.next();
            if (str.getEstimatedCost() < boundary && str.getscheduledNodes().size() <= boundaryState.getscheduledNodes().size()){
                //schedule();
            }else {
                iter.remove();
            }
        }
        if(_runtimeMonitor != null) {
            _runtimeMonitor.finish(boundaryState);
        }
        return boundaryState;
    }

    /**
     * This method will return next valid states based on the current state
     * @return Next valid state
     */
    public PriorityQueue<State> nextState() {
        if(this._numberOfCores == 0||this._currentStates.size()==1||this._currentStates.size()==2||this._currentStates.size()==0){
            State leastCostState = this._currentStates.peek();
            List<State> states = this._currentStates.poll().getAllPossibleNextStates(this._graph);
            if(leastCostState.getCost()==0){
                this._currentStates.add(states.get(0));
            }
            else{
                for (State st : states) {
                    this._currentStates.add(st);
                }}
        } else {
            State leastCostState = this._currentStates.peek();
            if(_runtimeMonitor != null) {
                _runtimeMonitor.updateOptimal(this._currentStates.peek());
            }
            List<String> sheduledNodesName =new ArrayList<String>();
            for (Node n : leastCostState.getscheduledNodes()){
                sheduledNodesName.add(n.getName());
            }
            Boolean same = true;
            List<State> states = new ArrayList<State>();
            while (same) {
                states.add(this._currentStates.poll());
                if (this._currentStates.isEmpty()||this._currentStates.peek().getCost() != leastCostState.getCost()||this._currentStates.peek().getscheduledNodes().size()!=leastCostState.getscheduledNodes().size()) {
                    same = false;
                }
            }

            Iterator<State> iter = states.iterator();
            while (iter.hasNext()) {
                List<String> demo =new ArrayList<String>();
                State str = iter.next();
                for (Node n : str.getscheduledNodes()){
                    demo.add(n.getName());
                }
                boolean remove = true;
                for(String str1:demo){
                   if(!sheduledNodesName.contains(str1)){
                       remove=false;
                   }
                }
                if (remove==true){
                    iter.remove();
                }
            }

            states.add(0,leastCostState);

            for (State s : states) {
                ExecutorService executorPool = Executors.newFixedThreadPool(_numberOfCores);
                Runnable task1 = () -> {
                    updateCurrentstates(s);
                };
                executorPool.submit(task1);
                executorPool.shutdown();
                try {
                    executorPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                }
            }
        }
        return this._currentStates;
    }

    /**
     * Synchronized method used to update the current state field in parallel
     * @param s
     */
    synchronized void updateCurrentstates(State s){
        List<State> nextStates = s.getAllPossibleNextStates(this._graph);
        if(_runtimeMonitor != null) {
            for(int i=0; i< nextStates.size(); i++) {
                _runtimeMonitor.incrementStatesExplored(); //Update the monitor
            }
        }
        if(s.getscheduledNodes().size()==0){
            this._currentStates.add(nextStates.get(0));
        }
        else{
            for (State st : nextStates) {
                this._currentStates.add(st);
            }}
    }

    /**
     * Synchronized method used to remove unnecessary states in parallel
     */
    synchronized void removeUnnecessaryStates(){
        Iterator<State> iter = _currentStates.iterator();
        while (iter.hasNext()) {
            State state1=iter.next();
            Iterator<State> iter2 = _currentStates.iterator();
            while (iter2.hasNext()) {
                State state2=iter2.next();
                boolean equal=true;
                List<String> sheduledNodesName1 =new ArrayList<String>();
                for (Node n : state1.getscheduledNodes()){
                    sheduledNodesName1.add(n.getName());
                }
                List<String> sheduledNodesName2 =new ArrayList<String>();
                for (Node n : state2.getscheduledNodes()){
                    sheduledNodesName2.add(n.getName());
                }

                for(String n1:sheduledNodesName1){
                    if(!sheduledNodesName2.contains(n1)){
                        equal=false;
                    }
                }
                if(state1.getCost()==state2.getCost()&& equal==true&&state1!=state2){
                    iter.remove();
                }

            }
        }
    }
}
