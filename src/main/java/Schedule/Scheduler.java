package Schedule;

import Graph.Graph;
import Input.InputHandler;
import Graph.Node;
import com.sun.xml.internal.bind.v2.TODO;

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
    }

    /**
     * This method calculates a valid schedule
     * @return Valid Schedule
     */
    public State schedule(){
        int boundary;
        while (_currentStates.peek().getscheduledNodes().size() != _graph.getNodes().size()) {
            //this._currentStates=removeUnnecessaryStates(this._currentStates);
            for (State s : this._currentStates){
                s.refreshReachableNodes();
            }
           // System.out.println("size:"+_currentStates.peek().getscheduledNodes().size()+"   cost:"+_currentStates.peek().getEstimatedCost());

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


            Boolean same = true;
            List<State> states = new ArrayList<State>();
            while (same) {
                states.add(this._currentStates.poll());
                if (this._currentStates.isEmpty()||this._currentStates.peek().getCost() != leastCostState.getCost()||this._currentStates.peek().getscheduledNodes().size()!=leastCostState.getscheduledNodes().size()) {
                    same = false;
                }
            }
            //states =removeUnnecessaryStates(states);

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
        //return this._currentStates;
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
