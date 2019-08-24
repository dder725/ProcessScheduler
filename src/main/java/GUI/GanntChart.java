package GUI;

import Schedule.Processor;
import Model.State;
import Model.Task;

import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import GUI.GanntChartBuilder.ExtraData;

import java.util.ArrayList;

public class GanntChart {
    AnchorPane pane;
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

        chart.setTitle("CPU Schedule");
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

        AnchorPane pane = new AnchorPane(chart);
        this.pane = pane;


    }

    public AnchorPane getPane() {
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

