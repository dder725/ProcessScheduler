package Input;

import java.io.FileNotFoundException;
import GUI.MainWindow;
import Model.*;
import Parser.DotFileParser;
import Schedule.RuntimeMonitor;
import Schedule.AStarScheduler;
import Model.State;
import Output.OutputHandler;
import Output.Output;
import javafx.application.Application;

public class TaskSchedule {
	private Graph graph;
	private Boolean Done = true ;
	private static State _finalSchedule;
	private static InputHandler input;
	private static RuntimeMonitor _runtimeMonitor;
	//Singleton pattern
	private static TaskSchedule _instance = null;
	private TaskSchedule(){
		if(_instance == null){
			_instance = this;
		}
	}
	public static TaskSchedule getInstance() {
		return _instance;
	}

	public State getSchedule(){
		return _finalSchedule;
	}


	public static void main(String[] args) {
		new TaskSchedule(); //Create a new instance

		String str = "Nodes_7_OutTree.dot";
		String str1 = "4";
		String[] MockInput = new String[2];
		MockInput[0] = str;
		MockInput[1] = str1;
		input = new InputHandler(MockInput);

		try {
			if(input.toVisualize()) {
				Application.launch(MainWindow.class);
			} else {
				runAlgorithm(input);
			}
			//TODO:transfer from final state to output
		} catch (Exception e1) {
			e1.printStackTrace();
		}}

		public static void runAlgorithm(InputHandler input) throws FileNotFoundException {
			DotFileParser parser = new DotFileParser();
			Graph g = parser.parseDotFile("/Users/zjq/IdeaProjects/Project1/src/main/resources/Nodes_11_OutTree.dot");
			AStarScheduler sch = new AStarScheduler(g,input);
			State finalState =  sch.schedule();
			System.out.println("The scheduled node of finalState: "+finalState.getscheduledNodes().size());
			_finalSchedule = finalState;
			System.out.println("The cost of the final state is " +finalState.getCost());
			Output output = new Output(input);
			OutputHandler outputHandler = new OutputHandler(finalState,g);
			String finalOutput = outputHandler.getFinalOutput();
			output.generateGraph(finalOutput, "dot");
		}

		public static InputHandler getInput(){
			return input;
		}
}



