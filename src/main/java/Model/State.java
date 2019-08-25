package Model;

import CostFunction.BottomLevelFunction;
import CostFunction.IdleTimeFunction;
import Schedule.Processor;
import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a State/Schedule of the program, It contains the processors which Tasks are scheduled on and the input graph.
 * It has an estimatedCost which indicate the possibility of this state being optimal in the long run.
 */
public class State implements Comparable<State>{
    public  int _estimatedCost;
    private List<Processor> _processors = new ArrayList<Processor>();
    private List<Node> _reachableNodes = new ArrayList<Node>();
    private List<Node> _allNodes = new ArrayList<Node>();
    private List<State> _possibleNextState = new ArrayList<State>();
    private List<Node> _scheduledNodes = new ArrayList<Node>();
    private List<Task> _allTasks = new ArrayList<Task>();//
    private HashMap<Integer, Integer> _bottomLevels;
    private int _cost = 0;
    private  Graph _graph = null;
    private IdleTimeFunction idleTimeFunction;
    private Node nodeTOSchedule;
    /**
     * initialize the first initial state depends on the number of processors
     * No node is scheduled at this stage
     * @param _numberOfProcessors
     */
    public State(int _numberOfProcessors, List<Node> allNodes, Graph g){
    	this._allNodes = allNodes;
    	this._graph = g;
        for(int i = 0; i < _numberOfProcessors;i++) {
            this._processors.add(new Processor(i));
        }
        _estimatedCost = Integer.MAX_VALUE;

    }

    /**
     * A new state is constructed by schedule a new node on one of the processors of the parent state
     * All of its field value will be cloned means they don't reference to the same object.
     * The newly scheduled node will be added to scheduledNodes field and scheduledTasks
     * @param nextNodeToSchedule
     * @param idOfProcessor
     */
    public State(State parentState, Node nextNodeToSchedule, int idOfProcessor){
        this.inheriteFieldValue(parentState);
        this._scheduledNodes.add(nextNodeToSchedule);
        Processor processorToSchedule = this._processors.get(idOfProcessor);
        int startTime = this.calculateTaskStartTime(idOfProcessor,nextNodeToSchedule);
        processorToSchedule.schedule(nextNodeToSchedule,startTime);
        for (Processor p : this._processors) {
            if(p.getEndTime() > this._cost){
                this._cost = p.getEndTime();
            }

        }
        nodeTOSchedule=nextNodeToSchedule;


        //this._estimatedCost = Math.max((this._cost + _graph.getBottomLevel(nextNodeToSchedule)),idleTimeFunction.calculate(this));
    }

    /**
     * This method finds all the nodes that have all their parent scheduled and create states by schedule each of them into
     * one of the processors.
     * @param
     * @return
     */
    public List<State> getAllPossibleNextStates(Graph g){
        Cloner cloner=new Cloner();
        State clonedParent = cloner.deepClone(this);
        this._allNodes = g.getNodes();
        for(Node nextNode:this._reachableNodes) {
            for(int i = 0; i < this._processors.size(); i++) {
                this._possibleNextState.add(new State(clonedParent, nextNode, i));
            }
            this._scheduledNodes.add(nextNode);
        }
        return this._possibleNextState;
    }

    /**
     * This method let child inherit field values from parents
     * @param parentState
     */
    public void inheriteFieldValue(State parentState){
    		Cloner cloner = new Cloner();
    		State mockParent = cloner.deepClone(parentState);
    		this._graph = mockParent.getGraph();
            this._processors = mockParent.getProcessors();
            this._scheduledNodes = mockParent.getscheduledNodes();
            this._allNodes = mockParent.getNodes();
            this._allTasks=mockParent.getAllTasks();
            this._cost = mockParent.getCost();
    }

    /**
     * Source nodes are the first pack of reachable nodes
     * @param graph
     */
    public void initializeReachableNodes(Graph graph) {
        this._reachableNodes = graph.getSourceNodes();
    }

    /**
     * refresh the reacheable nodes list every time we come to a new state
     */
    public void refreshReachableNodes(){
        this._reachableNodes.clear();
        for(Node n:this._allNodes){
            if(n.isReachable(this._scheduledNodes) && !this._scheduledNodes.contains(n)){
                this._reachableNodes.add(n);
            }
        }
    }


    public boolean existReachablenodes() {
        return !(this._reachableNodes.isEmpty());
    }

    /**
     * This method will calculate the earliest start_time for a specified node
     * @param processorID
     * @param node
     * @return
     */
    public int calculateTaskStartTime(int processorID, Node node){
        int startTime = 0;

        List<Node> parentNodes = new ArrayList<Node>();
        parentNodes.addAll(node.getParents());
        List<Task> Tasks = this._processors.get(processorID).getAllTasks();

        if (parentNodes.isEmpty() && Tasks.isEmpty()){

            return startTime;

        } else if (parentNodes.isEmpty() && !(Tasks.isEmpty())){

            int lastTask = Tasks.size();
            Task lastScheduledTask = Tasks.get(lastTask-1);
            startTime = lastScheduledTask.getEndTime();

        } else if (!(parentNodes.isEmpty()) && !(Tasks.isEmpty())){

            List<Node> nodesInTheProcessor = new ArrayList<Node>();

            for(Task t:Tasks){
                nodesInTheProcessor.add(t.getNode());
            }

            if (nodesInTheProcessor.containsAll(parentNodes)){
                startTime = Tasks.get(Tasks.size()-1).getEndTime();
            } else {
                List<Node> demo = parentNodes;

                demo.removeAll(nodesInTheProcessor);

                int mostWaitingTime = 0;

                //demo contains all the parent nodes which are not in the current processor
                for(Node n: demo){
                    int endTime = 0;
                    int waitingTime = 0;
                    List<Task> scheduledTasks = this.getAllTasks();

                    // find the endpoint of the parent of the node
                    for(Task t : scheduledTasks){
                        // find the task object of the parent
                        if(t.getNode().getName().equals(n.getName())){
                           endTime = t.getEndTime();
                        }
                    }

                    //get the waiting time of that parent
                    for (Node i : this._graph.getLinkEdges().keySet()){
                        if(n.getName()==i.getName()){
                            for(Dependency d : this._graph.getLinkEdges().get(i)){
                                if(node.getName().equals(d.getChild().getName())){
                                    waitingTime=d.getWeight();
                                }
                            }
                        }
                    }
                    if(endTime + waitingTime > mostWaitingTime){
                        mostWaitingTime = endTime+waitingTime;
                    }
                }
                startTime = Math.max(Tasks.get(Tasks.size()-1).getEndTime(), mostWaitingTime);
            }

        } else if (!(parentNodes.isEmpty()) && Tasks.isEmpty()){
            int mostWaitingTime = 0;

            //demo contains all the parent nodes which are not in the current processor
            for(Node n: parentNodes){
                int endTime = 0;
                int waitingTime = 0;
                List<Task> scheduledTasks = this.getAllTasks();

                // find the endpoint of the parent of the node
                for(Task t : scheduledTasks){
                    // find the task object of the parent
                    if(t.getNode().getName().equals(n.getName())){
                        endTime = t.getEndTime();
                    }
                }

                for (Node i : this._graph.getLinkEdges().keySet()){
                    if(n.getName()==i.getName()){
                        for(Dependency d : this._graph.getLinkEdges().get(i)){
                            if(node.getName().equals(d.getChild().getName())){
                                waitingTime=d.getWeight();
                            }
                        }
                    }
                }

                if(endTime + waitingTime > mostWaitingTime){
                    mostWaitingTime = endTime+waitingTime;
                }
            }
            startTime = mostWaitingTime ;
        }
        return startTime;
    }

    public Graph getGraph() {
        return _graph;
    }

    public int getCost(){
        return this._cost;
    }

    /**
     * This method returns all tasks scheduled in this state
     * @return
     */
    public List<Task> getAllTasks(){
        for(Processor p : this._processors){
            this._allTasks.addAll(p.getAllTasks());
        }
        return _allTasks;
    }

    public List<Processor> getProcessors(){
        return this._processors;
    }

    public List<Node> getNodes(){
        return this._allNodes;

    }

    public List<Node> getscheduledNodes(){
        return this._scheduledNodes;

    }

    public List<Node> getReachableNodes(){
        return this._reachableNodes;
    }

    @Override
	public int compareTo(State o) {
        if(this.getscheduledNodes().size()>=o.getscheduledNodes().size()&&this.getCost()<=o.getCost()){
            return -1;
        }else if(this.getEstimatedCost()<o.getEstimatedCost() && this.getscheduledNodes().size()>=o.getscheduledNodes().size()){
            return -1;
        }else{
            return this.getEstimatedCost()-o.getEstimatedCost();
        }
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(Task t:_allTasks) {
			str.append(t.getNode().getName());
		}
		return str.toString();
	}

    /**
     * return the estimated cost of the state
     * it is generated from one of the three cost functions
     * @return
     */
    public int getEstimatedCost() {
        int es = BottomLevelFunction.calculateBottom(this);

        IdleTimeFunction idleTimeFunction=new IdleTimeFunction(_processors.size(),_graph);

        es = Math.max(es,idleTimeFunction.calculate(this));
        return es;
    }
}
