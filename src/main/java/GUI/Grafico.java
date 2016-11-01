package GUI;

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
import java.awt.*;
import java.sql.Time;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by pedro on 29-10-2016.
 */
public class Grafico extends JFrame{
  private String title;
  private Monitor m;
  private int pontos;
  public static Dimension size = new Dimension(800,480);

  public enum TimeUnit {SECOND,MINUTE,HOUR}
  public enum DataUnit {B,KB,MB,GB}

  private TimeUnit timeUnits;
  private DataUnit dataUnits;

  public TimeUnit getTimeUnits() {
    return timeUnits;
  }
  public void setTimeUnits(TimeUnit timeUnits) {
    this.timeUnits = timeUnits;
  }
  public DataUnit getDataUnits() {
    return dataUnits;
  }
  public void setDataUnits(DataUnit dataUnits) {
    this.dataUnits = dataUnits;
  }
  public int getPontos() {
    return pontos;
  }
  public void setPontos(int pontos) {
    this.pontos = pontos;
  }

  public Grafico(Monitor m, String title, int pontos){
    this.title = title;
    this.m = m;
    this.timeUnits = TimeUnit.MINUTE;
    this.dataUnits = DataUnit.KB;
    this.pontos = pontos;
  }

  public ChartPanel getPane(){
    try {
      final ChartPanel chartPanel = createChartPanel(m.getIfStats().get(title));
      chartPanel.setPreferredSize(size);
      chartPanel.setMouseZoomable(true, false);
      return chartPanel;
    }
    catch (Exception e){
      return Grafico.getEmptyPane();
    }
  }

  public static ChartPanel getEmptyPane(){
    String chartTitle = "Sem Coneccao";
    String xAxisLabel = "Tempo";
    String yAxisLabel = "Octetos";

    XYDataset dataset = new XYSeriesCollection();

    JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,xAxisLabel,yAxisLabel,dataset,
        PlotOrientation.VERTICAL,true,false,false);

    ChartPanel ret = new ChartPanel(chart);
    ret.setPreferredSize(size);
    ret.setMouseZoomable(false);
    return ret;
  }

  private double getX(Ponto p){
    switch (this.timeUnits){
      case SECOND:
        return p.getX();
      case MINUTE:
        return p.getX()/60.0f;
      case HOUR:
        return p.getX()/3600.0f;
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
      case HOUR:
        return "h";
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
      synchronized (stats){
        Iterator<Ponto> itIn = stats.getPontosIn().descendingIterator();
        Iterator<Ponto> itOut = stats.getPontosOut().descendingIterator();
        for(int i = 0; i<this.pontos && itIn.hasNext() && itOut.hasNext(); i++) {
          Ponto pIn = itIn.next();
          Ponto pOut = itOut.next();
          in.add(this.getX(pIn), this.getY(pIn));
          out.add(this.getX(pOut), this.getY(pOut));
        }
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

    XYPlot plot = chart.getXYPlot();
    if(stats != null) {
      NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
      switch (this.timeUnits){
        case SECOND:
          xAxis.setRange(-(this.pontos-1) * stats.getPollTimeInSeconds(), 0);
          break;
        case MINUTE:
          xAxis.setRange(-(this.pontos-1) * stats.getPollTimeInMinutes(), 0);
          break;
        case HOUR:
          xAxis.setRange(-(this.pontos-1) * stats.getPollTimeInMinutes(), 0);
          break;
      }
      NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
      yAxis.setNumberFormatOverride(java.text.NumberFormat.getNumberInstance() );
    }
    plot.setRenderer(new XYLineAndShapeRenderer(true,true));

    return new ChartPanel(chart);
  }
}
