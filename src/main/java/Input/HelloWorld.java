package Input;

import java.io.FileNotFoundException;

import Graph.Graph;
import Parser.DotFileParser;
import Schedule.Scheduler;
import Schedule.State;

public class HelloWorld {
	private Graph graph;
	private Boolean Done = true ;


	public static void main(String[] args) {
		String str = "Nodes_7_OutTree.dot";
		String str1 = "2";
		String[] MockInput = new String[2];
		MockInput[0] = str;
		MockInput[1] = str1;
		InputHandler input = new InputHandler(MockInput);
		DotFileParser parser = new DotFileParser();
		System.out.println(System.getProperty("user.dir")+"/"+input.getFilePath());
		try {
			Graph g = parser.parseDotFile(System.getProperty("user.dir")+"/"+input.getFilePath());
			Scheduler sch = new Scheduler(g,input);
			State finalState = sch.schedule();
			System.out.println(finalState);
			System.out.println("The cost of the final state is " +finalState.getCost());
			//output from here
			//TODO:transfer from final state to output
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Please enter a valid file name and make sure the input dot file is in the same directory as your jar file");	
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}

}
