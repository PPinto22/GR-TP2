package GUI;

import Business.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

/**
 * Created by pedro on 29-10-2016.
 */
public class MainFrame extends JFrame{
  public JPanel rootPanel;
  public JPanel chartPanel;
  public JComboBox cbInterface;
  public JSlider sliderPontos;
  public JComboBox cbPolling;
  public JTextField portaTextField;
  public JTextField ipTextField;
  public JButton conectarButton;
  public JButton padraoButton;
  public JButton aplicarButton;
  public Monitor m;
  public Refresher refresher;
  public Grafico grafico;

  private void initMyComponents(){
    this.cbPolling.addItem("5 segundos");
    this.cbPolling.addItem("15 segundos");
    this.cbPolling.addItem("30 segundos");
    this.cbPolling.addItem("1 minuto");
    this.cbPolling.addItem("2 minutos");
    this.cbPolling.addItem("5 minutos");
    this.cbPolling.addItem("15 minutos");
    this.cbPolling.setSelectedItem("30 segundos");

    this.sliderPontos.setSnapToTicks(true);
    this.sliderPontos.setMajorTickSpacing(5);
    this.sliderPontos.setPaintTicks(true);
    this.sliderPontos.setPaintLabels(true);

    this.cbInterface.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        grafico = new Grafico(m,(String) cbInterface.getSelectedItem(),sliderPontos.getValue());
        setGrafico(grafico);
      }
    });

    this.aplicarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        // TODO
      }
    });

    this.padraoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        cbPolling.setSelectedItem("30 segundos");
        sliderPontos.setValue(25);
      }
    });

    // TODO: Tratar de enderecos errados
    this.conectarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        try{
          m.connect(ipTextField.getText(),Integer.parseInt(portaTextField.getText()));
        } catch (UnknownHostException e) {
          JOptionPane.showMessageDialog(MainFrame.this, "Endereco invalido");
          ipTextField.setText("127.0.0.1");
          portaTextField.setText("5555");
        }
        catch (NumberFormatException e){
          JOptionPane.showMessageDialog(MainFrame.this, "Porta invalida");
          ipTextField.setText("127.0.0.1");
          portaTextField.setText("5555");
        }
      }
    });
  }

  public MainFrame() throws InterruptedException, UnknownHostException {
    this.initMyComponents();

    m = new Monitor(5000,this.sliderPontos.getMaximum(),this);
    m.connect(ipTextField.getText(),Integer.parseInt(portaTextField.getText()));

    this.grafico = null;
    this.chartPanel.add(Grafico.getEmptyPane());

    this.startRefresher();
  }

  private void startRefresher(){
    this.refresher = new Refresher(this,chartPanel,grafico,m.getPoll()/3);
    this.refresher.start();
  }

  public long getPoll(){
    switch ((String)this.cbPolling.getSelectedItem()){
      case "5 segundos":
        return 1000*5;
      case "15 segundos":
        return 1000*15;
      case "30 segundos":
        return 1000*30;
      case "1 minuto":
        return 1000*60;
      case "2 minutos":
        return 1000*120;
      case "5 minutos":
        return 1000*300;
      case "15 minutos":
        return 1000*900;
    }
    return 1000;
  }

  public void setGrafico(Grafico g) {
    this.grafico = g;
    this.refresher.setGrafico(this.grafico);
    this.refresher.refresh(false);
  }

  public void connectionError(){
    JOptionPane.showMessageDialog(MainFrame.this, "Erro de coneccao. " +
        "Especifique um endereco IP e porta validos");
    synchronized (cbInterface){
      cbInterface.removeAllItems();
    }
    this.setGrafico(null);

  }

  // TODO: Anchor
  // TODO: Opcoes para mudar unidades dos eixos
  public static void main(String[] args) throws InterruptedException, UnknownHostException {
    JFrame frame = new JFrame("Monitor");
    frame.setContentPane(new MainFrame().rootPanel);
    //frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
