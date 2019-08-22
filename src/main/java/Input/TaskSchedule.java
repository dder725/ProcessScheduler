package Input;

import java.io.FileNotFoundException;

import GUI.MainWindow;
import Graph.*;
import Parser.DotFileParser;
import Schedule.Scheduler;
import Schedule.State;
import Output.OutputHandler;
import Output.Output;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import sun.applet.Main;

public class TaskSchedule {
	private Graph graph;
	private Boolean Done = true ;
	private static State _finalSchedule;
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
		InputHandler input = new InputHandler(MockInput);
		DotFileParser parser = new DotFileParser();

		try {
			Graph g = parser.parseDotFile("/home/twelve_koalas/IdeaProjects/ProcessScheduler/src/main/resources/Nodes_8_Random.dot");
			if(input.toVisualize()) {
				Application.launch(MainWindow.class);
			} else {
				runAlgorithm(g, input);
			}
			//TODO:transfer from final state to output
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Please enter a valid file name and make sure the input dot file is in the same directory as your jar file");
		} catch (Exception e1) {
			e1.printStackTrace();
		}}

		private static void runAlgorithm(Graph g, InputHandler input){
			Scheduler sch = new Scheduler(g,input);
			State state = sch.schedule();
			State finalState =  sch.schedule();
			_finalSchedule = finalState;
			System.out.println("The cost of the final state is " +finalState.getCost());
			Output output = new Output(input);
			OutputHandler outputHandler = new OutputHandler(finalState,g);
			String finalOutput = outputHandler.getFinalOutput();
			output.generateGraph(finalOutput, "dot");
		}
}


