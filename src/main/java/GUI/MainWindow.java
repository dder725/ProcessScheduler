package GUI;

import Input.TaskSchedule;
import Schedule.State;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sun.applet.Main;

public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) {
        TaskSchedule mainApp = TaskSchedule.getInstance();
        BorderPane root = new BorderPane();
        GanntChart gannt = new GanntChart();
        gannt.createGantt(mainApp.getSchedule());
        primaryStage.setScene(gannt.getScene());
        primaryStage.setTitle("ScheME");
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
