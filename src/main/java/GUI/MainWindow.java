package GUI;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        GanntChart gannt = new GanntChart();
        gannt.createGantt();
        primaryStage.setScene(gannt.getScene());
        primaryStage.setTitle("ScheME");
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
