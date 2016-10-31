package GUI;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by pedro on 30-10-2016.
 */
public class Refresher extends Thread {

  private MainFrame mainFrame;
  private JPanel panel;
  private Grafico grafico;
  private boolean terminado;
  private long refresh; // Tempo em ms entre refreshs
  private Set<String> interfaces;

  public synchronized boolean isTerminado() {
    return terminado;
  }
  public synchronized long getRefresh() {
    return refresh;
  }
  public synchronized void setRefresh(long refresh) {
    this.refresh = refresh;
  }
  public synchronized void setTerminado(boolean terminado){
    this.terminado = terminado;
  }

  public Refresher(MainFrame mainFrame, JPanel panel, Grafico grafico, long refresh){
    this.mainFrame = mainFrame;
    this.panel = panel;
    this.grafico = grafico;
    this.terminado = false;
    this.refresh = refresh;
    this.interfaces = new TreeSet<>();
  }

  public synchronized void refresh(boolean updateInterfaces) {
    ChartPanel novo = null;
    if(grafico != null)
      novo = grafico.getPane();
    else
      novo = Grafico.getEmptyPane();

    synchronized (this.panel) {
      this.panel.removeAll();
      this.panel.add(novo);
      this.panel.revalidate();
      this.panel.repaint();
    }

    if(updateInterfaces) {
      synchronized (mainFrame.cbInterface) {
        Set<String> newInterfaces = mainFrame.m.getIfStats().keySet();
        if (!newInterfaces.isEmpty() && this.interfaces.isEmpty()) {
          for (String s : newInterfaces) {
            this.interfaces.add(s);
            this.mainFrame.cbInterface.addItem(s);
          }
          mainFrame.cbInterface.setSelectedIndex(0);
        }
        if (!newInterfaces.equals(interfaces)) {
          mainFrame.cbInterface.removeAllItems();
          for (String s : newInterfaces) {
            mainFrame.cbInterface.addItem(s);
          }
        }
      }
    }
  }

  @Override
  public void run(){
    while(!terminado){
      this.refresh(true);
      try {
        Thread.sleep(this.refresh);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public synchronized void setGrafico(Grafico grafico) {
    this.grafico = grafico;
  }
}
