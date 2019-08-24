package Schedule;

import Graph.Graph;
import Graph.Node;
import Input.InputHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
        int counter = 0;
        while (_currentStates.peek().getscheduledNodes().size() != _graph.getNodes().size()) {
            for (State s : this._currentStates){
                s.refreshReachableNodes();
            }
            System.out.println("size:"+_currentStates.peek().getscheduledNodes().size()+"   cost:"+_currentStates.peek().getCost());
            nextState();
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

    //TODO
    // parallel there
    /**
     * This method will return next valid states based on the current state
     * @return Next valid state
     */
    public void nextState() {
        if(this._numberOfCores == 0||this._currentStates.size()==1||this._currentStates.size()==2||this._currentStates.size()==0){
            System.out.println("cores = "+_numberOfCores);
            List<State> states = this._currentStates.poll().getAllPossibleNextStates(this._graph);
            states = removeUnnecessaryStates(states);
            for (State s :states){
                this._currentStates.add(s);
            }

        }else {
            //List<Thread> threadPool = new ArrayList<Thread>();
            State leastCostState = this._currentStates.peek();

            System.out.println("zheli"+leastCostState.toString() + "     cost:"+leastCostState.getCost()+ "     escost:"+leastCostState.getEstimatedCost());

            if(_runtimeMonitor != null) {
                _runtimeMonitor.updateOptimal(this._currentStates.peek());
                System.out.println("Updated optimal schedule");
            }
            List<String> sheduledNodesName =new ArrayList<String>();
            //TODO:create a loop and get all states with the same cost as the first one
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
//
            Iterator<State> iter = states.iterator();
            while (iter.hasNext()) {
                List<String> demo =new ArrayList<String>();
                State str = iter.next();
                for (Node n : str.getscheduledNodes()){
                    demo.add(n.getName());
                }
                if (demo.equals(sheduledNodesName)){
                    iter.remove();
                }
            }

            states.add(0,leastCostState);

            for (State s : states) {
                //Executors.newFixedThreadPool(System.getRuntime().availableProcessors());
                ExecutorService executorPool = Executors.newFixedThreadPool(_numberOfCores);
//                executorPool.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                    //start thread
//                     updateCurrentstates(s);
//                    }
//                });
//                executorPool.shutdown();
//                try {
//                    executorPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//                } catch (InterruptedException e) {
//                }
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
    }

    synchronized void updateCurrentstates(State s){
        List<State> nextStates = s.getAllPossibleNextStates(this._graph);
        nextStates = removeUnnecessaryStates(nextStates);
        for (State st : nextStates) {
            this._currentStates.add(st);
        }
    }

    synchronized List<State> removeUnnecessaryStates( List<State> nextStates){
        PriorityQueue<State> candidates = new PriorityQueue<State>();
        List<State> result = new ArrayList<State>();

        for (State st : nextStates) {
            candidates.add(st);
            System.out.println(st.toString() + "     cost:"+st.getCost()+ "     escost:"+st.getEstimatedCost());
        }
        System.out.println("--------------------");
        while(!candidates.isEmpty()){
            State s = candidates.poll();
            result.add(s);
            Iterator<State> iter = candidates.iterator();
            List<Integer> demo1 = new ArrayList<Integer>();
            List<Integer> demo2 = new ArrayList<Integer>();
            while (iter.hasNext()) {
                State str = iter.next();
                for (Node n : s.getscheduledNodes()){
                    demo1.add(Integer.parseInt(n.getName()));
                }
                for (Node n : str.getscheduledNodes()){
                    demo2.add(Integer.parseInt(n.getName()));
                }
                if (demo1.containsAll(demo2)  && s.getCost() == str.getCost()){
                    iter.remove();
                }
                //if (demo1.containsAll(demo2)  && s.getCost() < str.getCost() && str.getEstimatedCost()==str.getCost() && s.getEstimatedCost()==s.getCost()){
                    //iter.remove();
                //}
            }
        }

        for (State st : result) {
            System.out.println(st.toString()+ "     cost:"+st.getCost());
        }
        System.out.println("***************");
        return result;
    }
}
