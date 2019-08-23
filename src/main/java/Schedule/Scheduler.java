package Schedule;

import Graph.Graph;
import Input.InputHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class Scheduler {
    private State _rootState;
    private  Graph _graph;
    private PriorityQueue<State> _currentStates = new PriorityQueue<State>();
    private int  _numberOfProcessors;
    private int _numberOfCores=0;
    private RuntimeMonitor _runtimeMonitor;

    /**
     * This method will initialize the current state with numbers of processors and reachable nodes.
     * @param g
     * @param i
     */
    public Scheduler(Graph g, InputHandler i){
        this._graph = g;
        this._numberOfProcessors = i.getNumberOfProcessors();
        this._rootState = new State(this._numberOfProcessors,g.getNodes(),g);
        this._rootState.initializeReachableNodes(this._graph);
        this._currentStates.add(this._rootState);
        this._numberOfCores = Integer.parseInt(i.getNumberOfCores());
        this._runtimeMonitor = RuntimeMonitor.getInstance();

        if(_runtimeMonitor != null) {
            _runtimeMonitor.start(_numberOfProcessors, _numberOfCores, _rootState);
            System.out.println("STARTED A MONITOR");
        }

    }

    /**
     * This method calculates a valid schedule
     * @return Valid Schedule
     */
    public State schedule(){

        int boundary;

        while (_currentStates.peek().getscheduledNodes().size() != _graph.getNodes().size()) {
            System.out.println("Bbefore: "+_currentStates.size());
            for (State s : this._currentStates){
                s.refreshReachableNodes();
            }
            System.out.println("before: "+_currentStates.size());
            this._currentStates =  this.nextState();

            System.out.println("after: "+_currentStates.size());

        }

        State boundaryState = this._currentStates.poll();
        boundary = boundaryState.getCost();
        Iterator<State> iter = this._currentStates.iterator();
        while (iter.hasNext()) {
            State str = iter.next();
            if (str.getEstimatedCost() == boundary && str.getscheduledNodes().size() < boundaryState.getscheduledNodes().size()){
                schedule();
            }else {
                iter.remove();
            }
        }
        return boundaryState;
    }

    //TODO
    // parallel there
    /**
     * This method will return next valid states based on the current state
     * @return Next valid state
     */
    public PriorityQueue<State> nextState() {
        if(this._numberOfCores == 0||this._currentStates.size()==1||this._currentStates.size()==2||this._currentStates.size()==0){
            System.out.println("cores = "+_numberOfCores);
            List<State> states = this._currentStates.poll().getAllPossibleNextStates(this._graph);
            for (State s :states){
                this._currentStates.add(s);
            }

        }else {
            //List<Thread> threadPool = new ArrayList<Thread>();
            State leastCostState = this._currentStates.peek();
            if(_runtimeMonitor != null) {
                _runtimeMonitor.updateOptimal(this._currentStates.peek());
                System.out.println("Updated optimal schedule");
            }
            //TODO:create a loop and get all states with the same cost as the first one
            Boolean same = true;
            List<State> states = new ArrayList<State>();
            while (same) {
                states.add(this._currentStates.poll());
                if (this._currentStates.isEmpty()||this._currentStates.peek().getCost() != leastCostState.getCost()) {
                    same = false;
                }
            }
            for (State s : states) {
                //Executors.newFixedThreadPool(System.getRuntime().availableProcessors());
                ExecutorService executorPool = Executors.newFixedThreadPool(_numberOfCores);
                executorPool.execute(new Runnable() {
                    @Override
                    public void run() {
                    //start thread
                     updateCurrentstates(s);
                    }
                });
                executorPool.shutdown();
                try {
                    executorPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                }
            }


        }
        return this._currentStates;
    }

    synchronized void updateCurrentstates(State s){
        List<State> nextStates = s.getAllPossibleNextStates(this._graph);
        for (State st : nextStates) {
            this._currentStates.add(st);
        }
    }
}
