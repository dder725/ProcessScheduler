package Parser;

import Graph.Graph;
import Graph.GraphBuilder;
import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;

import java.io.*;

/**
 * This class is aimed to parse an input .dot file for its further processing
 */

public class DotFileParser {
    GraphParser parser;

    public DotFileParser(){
    }

    /**
    Method for parsing a dot file
    @param pathToDotFile Filepath of the dot file to parse
     **/
    public Graph parseDotFile(String pathToDotFile) throws FileNotFoundException {

        parser = new GraphParser(new FileInputStream(pathToDotFile));

        GraphBuilder graphBuilder = new GraphBuilder(parser.getGraphId());

        //Add all nodes parsed by a library function
        for(GraphNode node : parser.getNodes().values()){
            graphBuilder.addNode(node.getId(), Integer.parseInt(node.getAttribute("Weight").toString()));
        }

        //Add all edges parsed by a library function
        for(GraphEdge edge : parser.getEdges().values()){
            graphBuilder.addDependency(edge.getNode1().getId(), edge.getNode2().getId(), Integer.parseInt(edge.getAttribute("Weight").toString()));
        }
    return graphBuilder.createGraph();
    }

    /**
     * Method for parsing an individual line from a dot file
     * @param line Line to parse
     * @return
     */
    private String parseLine(String line){
        //Remove any whitespaces in the line
        String parsedLine = line.trim().replaceAll("\\s+","");
        return parsedLine;
    }

    /**
     * Invalid file format exception
     */
    public class InvalidFormatting extends java.lang.Throwable
    {
        /** The underlying error of this instance */
        public final static String Error = "An invalid format has been detected";

        /** Create a new {@code InvalidFormatting} exception */
        public InvalidFormatting() {
            super(InvalidFormatting.Error);
        }

        /**
         * Create a new {@code InvalidFormatting} exception with additional information
         *
         * @param additionalMsg The additional information to append
         */
        public InvalidFormatting(String additionalMsg) {
            super(InvalidFormatting.Error + "\n" + additionalMsg);
        }
    }
}
