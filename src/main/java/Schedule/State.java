package Schedule;

import Graph.Dependency;
import Graph.Graph;
import Graph.Node;
import Graph.GraphBuilder;
import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;

public class State implements Comparable<State>{
    private List<Processor> _processors = new ArrayList<Processor>();
    private List<Node> _reachableNodes = new ArrayList<Node>();
    private List<Node> _allNodes = new ArrayList<Node>();
    private List<State> _possibleNextState = new ArrayList<State>();
    private List<Node> _scheduledNodes = new ArrayList<Node>();
    private List<Task> _allTasks = new ArrayList<Task>();//
    private int _cost = 0;
    private  Graph _graph = null;
    private Node _lastScheduledNode;
    private  GraphBuilder _graphB = new GraphBuilder();

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
        this._lastScheduledNode = nextNodeToSchedule;
        this._cost = processorToSchedule.getEndTime();
    }


    public Node getlastScheduledNode() {
        return _lastScheduledNode;
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
            if(n.isReachable(this._scheduledNodes) && !_scheduledNodes.contains(n)){
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
            System.out.println(node.getParents().size());
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
                System.out.println("parentsize:"+demo.size()+demo.get(0).getName());

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
                //startTime = Tasks.get(Tasks.size()-1).getEndTime() + mostWaitingTime;
                startTime = mostWaitingTime;
            }
        } else if (!(parentNodes.isEmpty()) && Tasks.isEmpty()){


            int mostWaitingTime = 0;
            int st = 0;

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
                System.out.println("Endtime:"+endTime);
                System.out.println("Waitingtime"+ waitingTime);

            }
            st = mostWaitingTime;
            System.out.println("StartTime:"+st+"for node:"+node.getName());
            return st;
        }
        System.out.println("result:"+startTime);
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
		// TODO Auto-generated method stub
		int thisCost = this.getCost();
		int oCost = o.getCost();
		if(thisCost<oCost) {
			return -1;
		}else if(thisCost>oCost) {
			return 1;
		}
		return 0;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(Task t:_allTasks) {
			str.append(t.getNode().getName());
		}
		return str.toString();
		
	}
}
