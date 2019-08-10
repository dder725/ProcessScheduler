package Output;
import com.sun.javafx.geom.Edge;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
public class Output {
    private static final String TEMP_PATH = "/Users/zjq/IdeaProjects/Project1/src/main/resources/name.";

    public static void generateGraph(String dot, String graphType) {
        File out = new File(TEMP_PATH + graphType);   // Linux
        try{
            FileWriter fw=new FileWriter(out);
            fw.write("digraph \" Output \" {\n");
            fw.write(dot);
            fw.write("\n}");
            fw.close();
        }catch(Exception e){System.out.println(e);}


    }

}
