import javax.swing.*;

/**
 * Created by pedro on 30-10-2016.
 */
public class Refresher extends Thread {

  private JPanel panel;
  private Grafico grafico;
  private boolean terminado;
  private long refresh; // Tempo em ms entre refreshs

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

  public Refresher(JPanel panel, Grafico grafico, long refresh){
    this.panel = panel;
    this.grafico = grafico;
    this.terminado = false;
    this.refresh = refresh;
  }

  @Override
  public void run(){
    while(!terminado){
      //TODO: Apenas alterar dados em vez de desenhar novo grafico
      JPanel novo = grafico.getPane();
      this.panel.removeAll();
      this.panel.add(novo);
      this.panel.revalidate();
      this.panel.repaint();
      try {
        Thread.sleep(this.refresh);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
