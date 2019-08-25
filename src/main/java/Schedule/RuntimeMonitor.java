package Schedule;

import Input.TaskSchedule;
import Model.State;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.List;

//import scala.sys.Prop;

/*
Used to pass information to visualization
 */
public class RuntimeMonitor implements Observable {
    public static final double MILLISECONDS_PER_SECOND = 1000.0;

    private volatile boolean started;
    private volatile boolean finished;

    private volatile int statesExplored;
    private volatile int bestStates;
    private volatile int statesPruned;

    private volatile int numberOfProcessors;
    private volatile int numberOfCores;

    private volatile long startTime;
    private volatile long finishTime;
    private long totalTime;

    private static RuntimeMonitor _instance = null;


    private List<InvalidationListener> listeners = new ArrayList<>();
    private volatile State optimalSchedule;
    private volatile int optimalScheduleCost;

    public int getStatesExplored() {
        return statesExplored;
    }

    public int getBestStates() {
        return bestStates;
    }

    public int getNumberOfProcessors() {
        return numberOfProcessors;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public int getStatesDeleted(){
        return statesPruned;
    }

    public RuntimeMonitor(){
        this.finished = false;
        this.statesExplored = 0;
        this.bestStates = 0;
;
        this.totalTime = 0;
        this.optimalScheduleCost = 0;
        this.numberOfCores = 1;
        this.numberOfProcessors = TaskSchedule.getInput().getNumberOfProcessors();
        this.optimalSchedule = new State(numberOfProcessors, null, null);

        if(_instance == null){
            _instance = this;
        }

    }

    /*
    Set up the initial values once the algorithm had s
     */
    public void start(int numberOfProcessors, int numberOfCores, State rootState){
        this.started = true;
        this.numberOfProcessors = numberOfProcessors;
        this.numberOfCores = numberOfCores;
        this.startTime = System.currentTimeMillis();
        this.optimalSchedule = rootState;
        this.optimalScheduleCost = optimalSchedule.getCost();
    }

    public void finish(State optimalSchedule) {
        this.finished = true;
        this.optimalSchedule = optimalSchedule;
        this.finishTime = System.currentTimeMillis();
        this.totalTime = this.finishTime - this.startTime;
        updateOptimal(optimalSchedule);
    }
    public double getElapsedTime(){
        long elapsedTime;
        if(startTime > 0) {
             elapsedTime = System.currentTimeMillis() - startTime;
            totalTime = (long)(elapsedTime / MILLISECONDS_PER_SECOND);
        } else {
            totalTime = startTime;
        }
        return totalTime;
    }

    public void updateOptimal(State newOptimal) {
        this.optimalSchedule = newOptimal;
        if(this.optimalScheduleCost < this.optimalSchedule.getCost()) { //Only update if a new schedule has a better cost
            this.optimalScheduleCost = this.optimalSchedule.getCost();
        }
        this.bestStates++;
        invalidateListeners();
    }

    public State getOptimal(){
        return this.optimalSchedule;
    }

    public void finished(){
        this.finished = true;
        invalidateListeners();
    }

    public boolean isFinished(){
        return finished;
    }

    public int getTotalOptimalStates(){
        return this.bestStates;
    }

    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    public int getOptimalScheduleCost(){
        return optimalScheduleCost;
    }

    public static RuntimeMonitor getInstance(){
        return _instance;
    }

   private void invalidateListeners() {
           for (InvalidationListener listener : listeners) {
                listener.invalidated(this);
           }
     }

     public void incrementStatesExplored(){
        statesExplored++;
     }

     public void incrementStatesDeleted(){
        statesPruned++;
     }


     /*
     Reset the monitor to its original values for another algorithm run
      */
    public void resetRuntimeMonitor() {
        this.finished = false;
        this.statesExplored = 0;
        this.bestStates = 0;
        this.totalTime = 0;
        this.optimalScheduleCost = 0;
        this.numberOfCores = 1;
        this.numberOfProcessors = TaskSchedule.getInput().getNumberOfProcessors();
        this.optimalSchedule = new State(numberOfProcessors, null, null);
    }

    }


