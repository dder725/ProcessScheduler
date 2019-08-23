package GUI;

import Graph.Node;
import Schedule.Processor;
import Schedule.State;
import Schedule.Task;
import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import GUI.GanntChartBuilder.ExtraData;
import sun.plugin.javascript.navig.Anchor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GanntChart {
    BorderPane pane;
    GanntChartBuilder<Number, String> chart;
    ArrayList<String> processors = new ArrayList<String>();
    ArrayList<XYChart.Series> processorSeries = new ArrayList<XYChart.Series>();

    //Set up a blank Gannt chart
    public void createGantt(State state) {

        for (Processor processor : state.getProcessors()) {
            processors.add(Integer.toString(processor.getId()));
            processorSeries.add(new XYChart.Series());
        }
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        chart = new GanntChartBuilder<Number, String>(xAxis, yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.observableArrayList(processors));

        chart.setTitle("Optimal CPU Schedule");
        chart.setLegendVisible(false);
        chart.setBlockHeight(50);
        String machine;
        int currentSeries = 0;
        for (Processor processor : state.getProcessors()) {
            for (Task task : processor.getAllTasks()) {
                processorSeries.get(currentSeries).getData().add(new XYChart.Data(task.getStartTime(), processors.get(currentSeries), new ExtraData(task.getEndTime() - task.getStartTime(), "status-green", task.getNode().getName())));
            }
            currentSeries++;
        }

        for(XYChart.Series series: processorSeries){
            chart.getData().add(series);
        }
        chart.getStylesheets().add(getClass().getClassLoader().getResource("ganttchart.css").toExternalForm());

        BorderPane pane = new BorderPane(chart);
        this.pane = pane;


    }

    public BorderPane getPane() {
        return pane;
    }

    public void clear(){
        chart.getData().forEach(series ->{series.getData().clear();});
    }

    public void updateGantt(State state){
        int currentSeries = 0;
        for (Processor processor : state.getProcessors()) {
            for (Task task : processor.getAllTasks()) {
                processorSeries.get(currentSeries).getData().add(new XYChart.Data(task.getStartTime(), processors.get(currentSeries), new ExtraData(task.getEndTime() - task.getStartTime(), "status-green", task.getNode().getName())));
            }
            currentSeries++;
        }
    }
}

