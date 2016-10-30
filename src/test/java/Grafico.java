import Business.IfStats;
import Business.Monitor;
import Business.Ponto;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pedro on 29-10-2016.
 */
public class Grafico extends JFrame{
  private JPanel panel1;
  private String title;
  private Monitor m;

  public enum TimeUnit {SECOND,MINUTE}
  public enum DataUnit {B,KB,MB,GB}

  private TimeUnit timeUnits;
  private DataUnit dataUnits;

  public synchronized TimeUnit getTimeUnits() {
    return timeUnits;
  }
  public synchronized void setTimeUnits(TimeUnit timeUnits) {
    this.timeUnits = timeUnits;
  }
  public synchronized DataUnit getDataUnits() {
    return dataUnits;
  }
  public synchronized void setDataUnits(DataUnit dataUnits) {
    this.dataUnits = dataUnits;
  }

  public Grafico(Monitor m, String title){
    this.title = title;
    this.m = m;
    this.timeUnits = TimeUnit.MINUTE;
    this.dataUnits = DataUnit.KB;
  }

  public ChartPanel getPane(){
    final ChartPanel chartPanel = createChartPanel(m.getIfStats().get(title));
    chartPanel.setPreferredSize(new java.awt.Dimension(800, 480));
    chartPanel.setMouseZoomable(true, true);
    return chartPanel;
  }

/*  private XYDataset createDataset() {
    final TimeSeriesCollection dataset = new TimeSeriesCollection();

    Hour h = new Hour();
    final TimeSeries s1 = new TimeSeries("Input", Second.class);
    s1.add(new Second(-30,0,0,1,12,2000), 1.2);
    s1.add(new Second(-0,1,0,1,12,2000), 3.0);
    s1.add(new Second(-30,1,0,1,12,2000), 8.0);

    final TimeSeries s2 = new TimeSeries("Output", Second.class);
    s2.add(new Second(-30,0,0,1,12,2000), 0.0);
    s2.add(new Second(-0,1,0,1,12,2000), 0.0);
    s2.add(new Second(-30,1,0,1,12,2000), 0.0);

    dataset.addSeries(s1);
    dataset.addSeries(s2);

    return dataset;
  }*/

  private double getX(Ponto p){
    switch (this.timeUnits){
      case SECOND:
        return p.getX();
      case MINUTE:
        return p.getX()/60.0f;
      default:
        return 0;
    }
  }
  private String getXTitle(){
    switch (this.timeUnits){
      case MINUTE:
        return "min";
      case SECOND:
        return "s";
      default:
        return "";
    }
  }
  private String getYTitle(){
    switch (this.dataUnits){
      case B:
        return "Byte";
      case KB:
        return "kB";
      case MB:
        return "MB";
      case GB:
        return "GB";
      default:
        return "";
    }
  }
  private double getY(Ponto p){
    switch (this.dataUnits){
      case B:
        return p.getY();
      case KB:
        return p.getY()/1000.0f;
      case MB:
        return p.getY()/1000000.0f;
      case GB:
        return p.getY()/1000000000.0f;
      default:
        return 0;
    }
  }

  private XYDataset createDataset(IfStats stats){
    final XYSeriesCollection dataset = new XYSeriesCollection();
    final XYSeries in = new XYSeries("Input");
    final XYSeries out = new XYSeries("Output");

    if(stats != null) {
      for (Ponto p : stats.getPontosIn()) {
        in.add(this.getX(p),this.getY(p));
      }
      for (Ponto p : stats.getPontosOut()) {
        out.add(this.getX(p),this.getY(p));
      }
    }

    dataset.addSeries(in);
    dataset.addSeries(out);

    return dataset;
  }

  private ChartPanel createChartPanel(IfStats stats) {
    String chartTitle = "Interface \'"+title+"\'";
    String xAxisLabel = "Tempo ("+getXTitle()+")";
    String yAxisLabel = "Octetos ("+getYTitle()+")";

    XYDataset dataset = createDataset(stats);

    JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,xAxisLabel,yAxisLabel,dataset,
        PlotOrientation.VERTICAL,true,false,false);

/*    JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
        xAxisLabel, yAxisLabel, dataset, true,true,false);

    DateAxis axis = (DateAxis) plot.getDomainAxis();
    axis.setDateFormatOverride(new SimpleDateFormat("mm:ss"));*/

    XYPlot plot = chart.getXYPlot();
    if(stats != null) {
      NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
      xAxis.setRange(-(stats.getMaxp()) * stats.getPollTimeInMinutes(), 0);
    }
    plot.setRenderer(new XYLineAndShapeRenderer(true,true));

    return new ChartPanel(chart);
  }
}
