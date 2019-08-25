package GUI;

import Input.TaskSchedule;
import Schedule.RuntimeMonitor;
import Model.State;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.applet.Main;
import javafx.event.Event;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindowController implements Initializable, InvalidationListener {
    private static RuntimeMonitor _monitor;
    private int bestScheduleCost;
    private GanntChart gannt;
    private Stage _primaryStage;
    TaskSchedule _mainApp;
    private static RuntimeMonitor _runtimeMonitor;
    @FXML
    private BorderPane GanttPane;
    @FXML
    private Button startButton;
    @FXML
    private Label memoryLabel;
    @FXML
    private Label timeLabel;


    public MainWindowController(){
        _mainApp = TaskSchedule.getInstance();
        _runtimeMonitor = new RuntimeMonitor();
        bestScheduleCost = 0;
    }

    public static RuntimeMonitor getMonitor(){
        return _monitor;
    }

    @FXML
    public void startAlgorithm(){
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                _mainApp.runAlgorithm(_mainApp.getInput());
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }


    public void initializeGantt() {
        gannt = new GanntChart();
        gannt.createGantt(_runtimeMonitor.getOptimal());
        GanttPane.setCenter(gannt.getPane());
    }

    private void initializeBestScheduleUpdate() {

        Timeline updateBestSchedule = new Timeline(
                new KeyFrame(Duration.millis(50), (ActionEvent e) -> {
                    int newBestScheduleLength = _runtimeMonitor.getOptimalScheduleCost();

                        // only update if the best schedule is different
                        bestScheduleCost = newBestScheduleLength;
                        State newSchedule = _runtimeMonitor.getOptimal();
                        updateGanttChart(newSchedule);
                }
                ));
        updateBestSchedule.setCycleCount(Timeline.INDEFINITE);
        updateBestSchedule.play();
    }

    public void updateGantt(){
        gannt.clear();
        gannt.updateGantt(_runtimeMonitor.getOptimal());
    }

    public void updateGanttChart(State schedule){
        gannt.clear();
        gannt.updateGantt(schedule);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _runtimeMonitor.addListener(this);

        Platform.runLater(() -> {
            initializeGantt();
        });


    }

    @Override
    public void invalidated(Observable observable) {
        Platform.runLater(this::updateGantt);
    }
}

