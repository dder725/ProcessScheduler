/**
 * This class write the dot file and create the output
 * @author zjq
 *
 */
public class ScheduleBuilder {

private list<States> ls = new List<States>;
private list<Processor> lp = new List<Processor>;

public static void createDotGraph(String dotFormat,String fileName)
{
    GraphViz gv=new GraphViz();
    gv.addln(gv.start_graph());
    gv.add(dotFormat);
    gv.addln(gv.end_graph());
   // String type = "gif";
    String type = "dot";
  // gv.increaseDpi();
    gv.decreaseDpi();
    gv.decreaseDpi();
    File out = new File(fileName+"."+ type); 
    gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
}

}
