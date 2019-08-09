package Input;

import Graph.Graph;
import Schedule.Scheduler;
import Schedule.State;

public class HelloWorld {
	private static Graph graph;
	private Boolean Done = true ;


	public static void main(String[] args) {
		InputHandler input = new InputHandler(args);
		//get graph Graph graph = GraphBuilder.getGraph();
		Scheduler sch = new Scheduler(graph,input);
		State finalState = sch.schedule();
		//output from here
		//TODO:transfer from final state to output
		
	}

}
