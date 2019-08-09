package Schedule;

import Graph.Graph;
import Graph.Node;
import Graph.Dependency;
import java.awt.peer.ChoicePeer;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class State {
    private List<Processor> _processors = new ArrayList<Processor>();
    private List<Node> _reachableNodes = new ArrayList<Node>();
    private List<Node> _allNodes = new ArrayList<Node>();
    private List<State> _possibleNextState = new ArrayList<State>();
    private int _cost;
    private List<Node> scheduledNodes = new ArrayList<Node>();
    private List<Task> allTasks = new ArrayList<Task>();
    private  Graph _graph;


    public State(){

    }

    /**
     * initialize the first state depends on the number of processors
     * @param _numberOfProcessors
     */
    public State(int _numberOfProcessors){
        for(int i = 0; i < _numberOfProcessors;i++) {
            _processors.add(new Processor(i));
        }
    }

    /**
     *
     */
    public void initialSchedule(){


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
        Processor processorToSchedule = _processors.get(idOfProcessor);
        int startTime = this.calculateTaskStartTime(idOfProcessor,nextNodeToSchedule);
        processorToSchedule.schedule(nextNodeToSchedule,startTime);
    }

    /**
     * No function
     * @param parentState
     */
    public void inheriteFieldValue(State parentState){
            this._processors = parentState._processors;
            this.scheduledNodes = parentState.scheduledNodes;
    }

    /**
     * this method returns every possible state from the current state in the scheduler
     * @param
     * @return
     */
    public List<State> getAllPossibleNextStates(Graph g){
        setGraph(g);
        this._allNodes = g.getNodes();
        for(Node nextNode:this._reachableNodes) {
            for(int i = 0; i < _processors.size(); i++) {
                this._possibleNextState.add(new State(this, nextNode, i));
            }
            _reachableNodes.remove(nextNode);
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
    }

    /**
     * refresh the reacheable nodes list every time we come to a new state
     */
    public void refreshReachableNodes(){
        this._reachableNodes.clear();
        for(Node n:this._allNodes){
            if(n.isReachable(this.scheduledNodes)){
                this._reachableNodes.add(n);
            }
        }
    }

    /**
     * This method will calculate the start_time for a specified node
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
            Task lastScheduledTask = Tasks.get(lastTask);
            startTime = lastScheduledTask.getEndTime();
        } else if (!(parentNodes.isEmpty()) && !(Tasks.isEmpty())){
            List<Node> nodesInTheProcessor = new ArrayList<Node>();

            for(Task t:Tasks){
                nodesInTheProcessor.add(t.getNode());
            }

            if (nodesInTheProcessor.containsAll(parentNodes)){
                startTime = Tasks.get(Tasks.size()).getEndTime();
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


}
