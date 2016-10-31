package Business;

import GUI.MainFrame;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by pedro on 29-10-2016.
 */
public class Polling extends Thread{

  private long intervalo;
  private Monitor monitor;
  private boolean terminado;
  private MainFrame mainFrame;

  public Polling(Monitor monitor, long intervalo){
    this.intervalo = intervalo;
    this.monitor = monitor;
    this.terminado = false;
    this.mainFrame = null;
  }

  public Polling(Monitor monitor, long intervalo, MainFrame mainFrame){
    this.intervalo = intervalo;
    this.monitor = monitor;
    this.terminado = false;
    this.mainFrame = mainFrame;
  }

  public synchronized void setIntervalo(long intervalo){
    this.intervalo = intervalo;
  }

  public synchronized void setTerminado(boolean terminado){
    this.terminado = terminado;
  }

  @Override
  public void run() {
    while(!terminado){
      try {
        this.monitor.addStats(this.monitor.getInterfaces());
        Thread.sleep(this.intervalo);
      } catch (IOException e) {
        if(this.mainFrame != null)
          mainFrame.connectionError();
        this.terminado = true;
      } catch (InterruptedException e) {
        e.printStackTrace();
        this.terminado = true;
      }
    }
  }
}
