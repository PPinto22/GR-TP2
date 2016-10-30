import Business.Monitor;

import javax.swing.*;
import java.awt.*;
import java.net.UnknownHostException;

/**
 * Created by pedro on 29-10-2016.
 */
public class MainFrame extends JFrame{
  private JPanel panel1;
  private JPanel listPanel;
  private JScrollPane scrollPane;
  private Monitor m;

  public MainFrame() throws InterruptedException, UnknownHostException {
    m = new Monitor();
    m.connect("localhost",5555);
    m.setPoll(1000*5);

    m.startPolling();
    Grafico graf = new Grafico(m,"wlp58s0");
    this.panel1.add(graf.getPane());
    new Refresher(panel1,graf,m.getPoll()/3).start();
/*    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(5,5,5,5);
    c.gridy++;
    this.listPanel.add(new Grafico("").getPane(),c);
    c.gridy++;
    this.listPanel.add(new Grafico("").getPane(),c);
    c.gridy++;*/
//    this.listPanel.add(new Grafico("").getPane(),c);
//    c.gridy++;
  }
  public static void main(String[] args) throws InterruptedException, UnknownHostException {
    JFrame frame = new JFrame("Monitor");
    frame.setContentPane(new MainFrame().panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

}
