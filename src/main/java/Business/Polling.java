package Business;

import java.io.IOException;

/**
 * Created by pedro on 29-10-2016.
 */
public class Polling extends Thread{

  private long intervalo;
  private Monitor monitor;
  private boolean terminado;

  public Polling(Monitor monitor, long intervalo){
    this.intervalo = intervalo;
    this.monitor = monitor;
    this.terminado = false;
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
        e.printStackTrace();
        this.terminado = true;
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
