package Schedule;

import Graph.Graph;
import Graph.Node;
import Graph.Dependency;
import java.awt.peer.ChoicePeer;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.rits.cloning.Cloner;

public class State implements Comparable<State>{
    private List<Processor> _processors = new ArrayList<Processor>();
    private List<Node> _reachableNodes = new ArrayList<Node>();
    private List<Node> _allNodes = new ArrayList<Node>();
    private List<State> _possibleNextState = new ArrayList<State>();
    private int _cost;
    private List<Node> scheduledNodes = new ArrayList<Node>();
    private List<Task> allTasks = new ArrayList<Task>();//never updated
    private  Graph _graph;


    public State(){

    }

    /**
     * initialize the first state depends on the number of processors
     * @param _numberOfProcessors
     */
    public State(int _numberOfProcessors, List<Node> _allNodes, Graph g){
    	this._allNodes = _allNodes;
    	this._graph = g;
        for(int i = 0; i < _numberOfProcessors;i++) {
            _processors.add(new Processor(i));
        }
//        System.out.println(_processors);
    }

    /**
     *
     */
    public void initialSchedule(){
    	int startTime = this.calculateTaskStartTime(0,_reachableNodes.get(0));
    	this._processors.get(0).schedule(_reachableNodes.get(0),startTime);
    	this.scheduledNodes.add(_reachableNodes.get(0));

    }

    public void setGraph(Graph graph){
        _graph = graph;
    }


    /**
     * takes the number of the processor, find it and then assign a task to its schedule
     * and we have a new state.
     * @param nextNodeToSchedule
     * @param idOfProcessor
     */
    public State(State parentState, Node nextNodeToSchedule, int idOfProcessor){
    	this.inheriteFieldValue(parentState);
//    	System.out.println("id of processor is: "+idOfProcessor);
        Processor processorToSchedule = _processors.get(idOfProcessor);
        int startTime = this.calculateTaskStartTime(idOfProcessor,nextNodeToSchedule);
        processorToSchedule.schedule(nextNodeToSchedule,startTime);
        this.scheduledNodes.add(nextNodeToSchedule);

    }

    /**
     * No function
     * @param parentState
     */
    public void inheriteFieldValue(State parentState){
    		Cloner cloner = new Cloner();
    		State mockParent = cloner.deepClone(parentState);
            this._processors = mockParent.getProcessors();
            this.scheduledNodes = mockParent.getscheduledNodes();
            this._allNodes = mockParent.getNodes();
            this._reachableNodes = mockParent.getReachableNodes();
    }
    
    
    public List<Processor> getProcessors(){
		return this._processors;
    	
    }
    
    public List<Node> getNodes(){
		return this._allNodes;
    	
    }
    public List<Node> getscheduledNodes(){
		return this.scheduledNodes;
    	
    }
    
    public List<Node> getReachableNodes(){
    	return this._reachableNodes;
    }

    /**
     * this method returns every possible state from the current state in the scheduler
     * @param
     * @return
     */
    public List<State> getAllPossibleNextStates(Graph g){
        setGraph(g);
        Cloner cloner=new Cloner();
        State clonedParent = cloner.deepClone(this);
        this._allNodes = g.getNodes();
        for(Node nextNode:this._reachableNodes) {
            for(int i = 0; i < _processors.size(); i++) {
                this._possibleNextState.add(new State(clonedParent, nextNode, i));
            }
            this.scheduledNodes.add(nextNode);
        }
        return this._possibleNextState;
    }

    /**
     * 
     * @return 
     */
    public int getCost(){
        return this._cost;
    }

    /**
     *
     * @return
     */
    public boolean existReachablenodes() {
        return this._reachableNodes.isEmpty();
    }

    /**
     * Source nodes are the first pack of reachable nodes
     * @param graph
     */
    public void initializeReachableNodes(Graph graph) {
    	
    	
        this._reachableNodes = graph.getSourceNodes();
        refreshReachableNodes();
        /*this._reachableNodes.remove(0);*/
        
        
    }

    /**
     * refresh the reacheable nodes list every time we come to a new state
     */
    public void refreshReachableNodes(){
        this._reachableNodes.clear();
        for(Node n:this._allNodes){
            if(n.isReachable(this.scheduledNodes) && !scheduledNodes.contains(n)){
                this._reachableNodes.add(n);
            }
        }
        
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
        List<Task> Tasks = _processors.get(processorID).get_scheduledTasks();

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
                getAllTasks();
                int leastWaitingTime = 0;

                //demo contains all the parent nodes which are not in the current processor
                for(Node n: demo){
                    int endTime = 0;
                    int waitingTime = 0;

                    // find the endpoint of the parent of the node
                    for(Task t : allTasks){
                        // find the task object of the parent
                        if(t.getNode().getName().equals(n.getName())){
                           endTime = t.getEndTime();
                        }
                    }
                    //get the waiting time of that parent
                    for(Dependency d : _graph.getLinkEdges().get(n)){
                        if(node.getName().equals(d.getChild().getName())){
                            waitingTime=d.getWeight();
                        }
                    }

                    if(endTime + waitingTime > leastWaitingTime){
                        leastWaitingTime = endTime+waitingTime;
                    }

                }
                startTime = leastWaitingTime;

            }
        }


        return startTime;
    }

    private void getAllTasks(){
        for(Processor p : _processors){
            allTasks.addAll(p.getAllTasks());
        }
        //return allTasks;
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
		for(Task t:allTasks) {
			str.append(t.getNode().getName());
		}
		return str.toString();
		
	}


}
