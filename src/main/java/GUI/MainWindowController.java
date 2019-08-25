package GUI;

import Input.TaskSchedule;
import Schedule.RuntimeMonitor;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/*
Controller class for the main GUI window
 */
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
    @FXML
    private Label graphName;
    @FXML
    private Label schedulesFoundLabel;
    @FXML
    private Label duplicateSchedulesLabel;
    @FXML
    private Label optimalSchedulesLabel;
    @FXML
    private Label optimalTimeLabel;
    @FXML
    private javafx.scene.image.ImageView ImageView;
    @FXML
    private Label statusLabel;


    public MainWindowController(){
        _mainApp = TaskSchedule.getInstance();
        _runtimeMonitor = new RuntimeMonitor();
        bestScheduleCost = 0;

    }

    public static RuntimeMonitor getMonitor(){
        return _monitor;
    }


    @FXML //Method set on "Find Schedule" button
    public void startAlgorithm(){
//        if(!_runtimeMonitor.isFinished()){
        _runtimeMonitor.resetRuntimeMonitor();
        statusLabel.setText("RUNNING...");
        statusLabel.setVisible(true);
            timer = new Timer();
            long startTime = System.currentTimeMillis();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // calculate current used memory
                    long freeMemory = Runtime.getRuntime().freeMemory();
                    long totalMemory = Runtime.getRuntime().totalMemory();
                    long usedMemory = totalMemory - freeMemory;

                    //Set up a timer
                    double timeElapsed = System.currentTimeMillis() - startTime;
                    timeElapsed = timeElapsed / 1000.0;
                    System.out.println(timeElapsed);
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

        //Run algorithm in a different thread
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

/*
Build the initial Gantt Chart
 */
    public void initializeGantt() {
        gannt = new GanntChart();
        gannt.createGantt(_runtimeMonitor.getOptimal());
        GanttPane.setCenter(gannt.getPane());
    }

    public void updateGUI(){
        gannt.clear();
        gannt.updateGantt(_runtimeMonitor.getOptimal());
        optimalSchedulesLabel.setText(Integer.toString(_runtimeMonitor.getTotalOptimalStates()));
        optimalTimeLabel.setText(Integer.toString(_runtimeMonitor.getOptimalScheduleCost()));
        schedulesFoundLabel.setText(Integer.toString(_runtimeMonitor.getStatesExplored()));
        duplicateSchedulesLabel.setText(Integer.toString(_runtimeMonitor.getStatesDeleted()));
        if(_runtimeMonitor.isFinished()){
            statusLabel.setText("FINISHED");
        }
    }


    /*
    Initialization of the Main Window
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphName.setText(_mainApp.getGraphName());
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

        //Listen to the changes in the Runtime Monitor
        _runtimeMonitor.addListener(this);

        //Run graph visualization in a separate thread
        Platform.runLater(() -> {
            initializeGantt();
            Thread thread = new Thread(){
                public void run(){
                    try {
                        Graphviz.fromFile(new File(_mainApp.getPath())).width(900).render(Format.PNG).toFile(new File( "output.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Image img = new Image("file:output.png");
                    ImageView.setImage(img);
                }
            };
            thread.start();
        });
    }

    /*
    Event handler for changes in RuntimeMonitor
     */
    @Override
    public void invalidated(Observable observable) {
        if(!_runtimeMonitor.isFinished()) {
            Platform.runLater(this::updateGUI);
        } else {
            Platform.runLater(this::updateGUI);
            //Stop the timer if the algorithm had finished
            timer.cancel();
            timer.purge();}
    }
}

