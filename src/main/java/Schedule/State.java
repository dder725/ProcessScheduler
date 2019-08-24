package Schedule;

import Graph.Dependency;
import Graph.Graph;
import Graph.Node;
import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class State implements Comparable<State>{
    private  int _estimatedCost;
    private List<Processor> _processors = new ArrayList<Processor>();
    private List<Node> _reachableNodes = new ArrayList<Node>();
    private List<Node> _allNodes = new ArrayList<Node>();
    private List<State> _possibleNextState = new ArrayList<State>();
    private List<Node> _scheduledNodes = new ArrayList<Node>();
    private List<Task> _allTasks = new ArrayList<Task>();//
    private HashMap<Integer, Integer> _bottomLevels;
    private int _cost = 0;
    private  Graph _graph = null;

    /**
     * initialize the first state depends on the number of processors
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
     * takes the number of the processor, find it and then assign a task to its schedule
     * and we have a new state.
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
        //this._estimatedCost =processorToSchedule.getAllTasks().get(processorToSchedule.getAllTasks().size()-1).getStartTime()+ _graph.getBottomLevel(nextNodeToSchedule);
        this._estimatedCost = Math.max(this._cost, processorToSchedule.getAllTasks().get(processorToSchedule.getAllTasks().size()-1).getStartTime()+ _graph.getBottomLevel(nextNodeToSchedule));
    }

    /**
     * this method returns every possible state from the current state in the scheduler
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

    /**
     *
     * @return
     */
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

	public int compareTo(State o) {

 //Fast version
        if(this.getEstimatedCost()<getTotalWeight()/2&&this.getscheduledNodes().size()>_graph.getNodes().size()/2){
            return 1;}
        if (this.getGraph().getNodes().size() == this.getscheduledNodes().size() && this.getCost() < o.getCost()){
            return -1;
        }
        if(this.getEstimatedCost() == o._estimatedCost && this.getscheduledNodes().size() > o.getscheduledNodes().size()){
            return -1;
        }
        return  this.getEstimatedCost() - o._estimatedCost;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(Node n:_scheduledNodes) {
			str.append(n.getName());
		}
		return str.toString();
	}

    public int getEstimatedCost() {
        return this._estimatedCost;
    }

    // ke yi you hua!!!!
    private int getTotalWeight(){
        int weight=0;
        for(Node n:_graph.getNodes()){
            weight=weight+n.getWeight();
        }
        return weight;
    }

    private int diffBetweenExpect(){
        return Math.abs(getEstimatedCost()-getTotalWeight()/2);
    }
    private int getRestWeight(){
        int est = 0;
        List<Node> notScheduledNodes = new ArrayList<Node>();
        notScheduledNodes = _graph.getNodes();
        notScheduledNodes.removeAll(_scheduledNodes);
        for(Node n:notScheduledNodes){
            est=est+n.getWeight();
        }

        return est;
    }




}
