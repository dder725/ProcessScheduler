package GUI;

import Input.TaskSchedule;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class MainWindowController {

    @FXML
    private AnchorPane GanttPane;

    TaskSchedule mainApp = TaskSchedule.getInstance();
    public void initializeGantt(){
        GanntChart gannt = new GanntChart();
        gannt.createGantt(mainApp.getSchedule());
        AnchorPane ganttPane = gannt.getPane();

        GanttPane.getChildren().add(ganttPane);
        ganttPane.setLayoutX(110);
        GanttPane.setMinSize(790, 411);
    }
    public void doNothing(){

    }
}
