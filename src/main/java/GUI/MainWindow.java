package GUI;

import Input.TaskSchedule;
import Schedule.State;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.applet.Main;

import java.io.IOException;

public class MainWindow extends Application {
    private static final int SCENE_HEIGHT = 645;
    private static final int SCENE_WIDTH = 995;
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("MainWindow.fxml"));
        Parent root = fxmlLoader.load();
        MainWindowController controller = fxmlLoader.getController();
        controller.initializeGantt();

        TaskSchedule mainApp = TaskSchedule.getInstance();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setHeight(SCENE_HEIGHT);
        primaryStage.setWidth(SCENE_WIDTH);
        primaryStage.setTitle("ScheME");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
