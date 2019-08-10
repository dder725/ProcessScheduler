package Input;

import java.io.FileNotFoundException;

import Graph.*;
import Parser.DotFileParser;
import Schedule.Scheduler;
import Schedule.State;
import Output.OutputHandler;
import Output.Output;
public class HelloWorld {
	private Graph graph;
	private Boolean Done = true ;


	public static void main(String[] args) {
//		String str = "Nodes_10_Random.dot";
//		String str1 = "2";
//		String[] MockInput = new String[2];
//		MockInput[0] = str;
//		MockInput[1] = str1;
//		InputHandler input = new InputHandler(MockInput);
		InputHandler input = new InputHandler(args);
		DotFileParser parser = new DotFileParser();
		System.out.println(System.getProperty("user.dir")+"/"+input.getFilePath());
		try {
			Graph g = parser.parseDotFile(System.getProperty("user.dir")+"/"+input.getFilePath());
			Scheduler sch = new Scheduler(g,input);
			State finalState = sch.schedule();
			System.out.println(finalState);
			System.out.println("The cost of the final state is " +finalState.getCost());
			Output output = new Output(input);
			OutputHandler outputHandler = new OutputHandler(finalState,g);
			String finalOutput = outputHandler.getFinalOutput();
			output.generateGraph(finalOutput, "dot");
			//output from here
			//TODO:transfer from final state to output
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Please enter a valid file name and make sure the input dot file is in the same directory as your jar file");	
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
//		InputHandler input = new InputHandler(args);
//		//get graph Graph graph = GraphBuilder.getGraph();
//		Scheduler sch = new Scheduler(graph,input);
//		State finalState = sch.schedule();
//
//		//output from here

//		//TODO:transfer from final state to output
		


	}

}
