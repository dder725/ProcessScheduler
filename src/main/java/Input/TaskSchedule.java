package Input;

import java.io.FileNotFoundException;

import GUI.MainWindow;
import Graph.*;
import Parser.DotFileParser;
import Schedule.Scheduler;
import Schedule.State;
import Output.OutputHandler;
import Output.Output;
import Schedule.Task;
import com.sun.org.apache.xpath.internal.objects.XNull;
import javafx.application.Application;
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
		String str1 = "2";
		String[] MockInput = new String[2];
		MockInput[0] = str;
		MockInput[1] = str1;
		InputHandler input = new InputHandler(MockInput);
		//InputHandler input = new InputHandler(args);
		DotFileParser parser = new DotFileParser();
		//System.out.println(System.getProperty("user.dir")+"/"+input.getFilePath());

		try {
			Graph g = parser.parseDotFile("/home/twelve_koalas/IdeaProjects/ProcessScheduler/src/main/resources/Nodes_11_OutTree.dot");
			Scheduler sch = new Scheduler(g,input);
			State finalState = sch.schedule();
			_finalSchedule = finalState;
//			System.out.println(finalState);
			System.out.println("The cost of the final state is " +finalState.getCost());
			Output output = new Output(input);
			OutputHandler outputHandler = new OutputHandler(finalState,g);
			String finalOutput = outputHandler.getFinalOutput();
			output.generateGraph(finalOutput, "dot");

			//MainWindow mainWindow = new MainWindow(finalState);
			javafx.application.Application.launch(MainWindow.class);

			//output from here
			//TODO:transfer from final state to output
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Please enter a valid file name and make sure the input dot file is in the same directory as your jar file");	
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
	public void launchGUI(State finalState){

	}
}
