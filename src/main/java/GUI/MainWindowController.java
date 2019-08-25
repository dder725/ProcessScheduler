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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
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
    private Timer timer;
    private int bestScheduleCost;
    private GanntChart gannt;
    private Stage _primaryStage;
    TaskSchedule _mainApp;
    private static RuntimeMonitor _runtimeMonitor;
    @FXML
    private MenuButton algorithmChoice;
    @FXML
    private MenuItem aStarChoice;
    @FXML
    private MenuItem branchBoundChoice;
    @FXML
    private BorderPane GanttPane;
    @FXML
    private Button startButton;
    @FXML
    private Label memoryTotalLabel;
    @FXML
    private Label memoryUsedLabel;
    @FXML
    private Label memoryFreeLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label timeUnits;


    public MainWindowController(){
        _mainApp = TaskSchedule.getInstance();
        _runtimeMonitor = new RuntimeMonitor();
        bestScheduleCost = 0;

    }

    public static RuntimeMonitor getMonitor(){
        return _monitor;
    }

    @FXML
    public void toggleAlgorithmChoice(){


    }
    @FXML
    public void startAlgorithm(){
        if(!_runtimeMonitor.isFinished()){
            timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // calculate current used memory
                    long freeMemory = Runtime.getRuntime().freeMemory();
                    long totalMemory = Runtime.getRuntime().totalMemory();
                    long usedMemory = totalMemory - freeMemory;

                    //Set up a timer
                    double timeElapsed = _runtimeMonitor.getElapsedTime();
                    memoryFreeLabel.setText((freeMemory)  / (1024l * 1024l) + "Mb");
                    memoryTotalLabel.setText(totalMemory / (1024l * 1024l) + "Mb");
                    memoryUsedLabel.setText(usedMemory / (1024l * 1024l) + "Mb");
                    timeLabel.setText(
                            (timeElapsed>60)?
                                    String.format("%d:%02.0f", (int)timeElapsed/60, timeElapsed%60)
                                    :
                                    String.format("%.2f", timeElapsed)
                    );
                    timeUnits.setText((timeElapsed>=60)?"MIN":"SEC");
                });
            }
        }, 0, 50);

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                _mainApp.runAlgorithm(_mainApp.getInput());
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        // update all the GUI information in a GUI thread
    } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "The optimal schedule had been already produced. Please relaunch the program using new parameters to find a schedule for a different graph", ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();}
    }

/*
Build the initial Gantt Chart
 */
    public void initializeGantt() {
        gannt = new GanntChart();
        gannt.createGantt(_runtimeMonitor.getOptimal());
        GanttPane.setCenter(gannt.getPane());
    }

    public void updateGantt(){
        gannt.clear();
        gannt.updateGantt(_runtimeMonitor.getOptimal());
    }


    /*
    Initialization of the Gantt chart
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set up algorithm toggle
        aStarChoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                algorithmChoice.setText(aStarChoice.getText());
                _mainApp.toggleAlgorithm(true);
            }
        });
        branchBoundChoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                algorithmChoice.setText(branchBoundChoice.getText());
                _mainApp.toggleAlgorithm(false);
            }
        });

        _runtimeMonitor.addListener(this);

        Platform.runLater(() -> {
            initializeGantt();
        });
    }

    /*
    Event handler for changes in RuntimeMonitor
     */
    @Override
    public void invalidated(Observable observable) {
        if(!_runtimeMonitor.isFinished()) {
            Platform.runLater(this::updateGantt);
        } else {
            Platform.runLater(this::updateGantt);
            //Stop the timer
            timer.cancel();
            timer.purge();}
    }
}

