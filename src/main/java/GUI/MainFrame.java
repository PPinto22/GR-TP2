package GUI;

import Business.Monitor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.TreeSet;

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
  private JRadioButton segundosRadioButton;
  private JRadioButton minutosRadioButton;
  private JRadioButton byteRadioButton;
  private JRadioButton kBRadioButton;
  private JRadioButton MBRadioButton;
  private JRadioButton GBRadioButton;
  private JRadioButton horasRadioButton;
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
    this.cbPolling.addItem("30 minutos");
    this.cbPolling.addItem("1 hora");
    this.cbPolling.setMaximumRowCount(10);
    this.cbPolling.setSelectedItem("30 segundos");

    this.sliderPontos.setSnapToTicks(true);
    this.sliderPontos.setMajorTickSpacing(5);
    this.sliderPontos.setPaintTicks(true);
    this.sliderPontos.setPaintLabels(true);

    this.segundosRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        deselectAllTimeUnits();
        segundosRadioButton.setSelected(true);
      }
    });
    this.minutosRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        deselectAllTimeUnits();
        minutosRadioButton.setSelected(true);
      }
    });
    this.horasRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        deselectAllTimeUnits();
        horasRadioButton.setSelected(true);
      }
    });

    this.byteRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        deselectAllDataUnits();
        byteRadioButton.setSelected(true);
      }
    });
    this.kBRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        deselectAllDataUnits();
        kBRadioButton.setSelected(true);
      }
    });
    this.MBRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        deselectAllDataUnits();
        MBRadioButton.setSelected(true);
      }
    });
    this.GBRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        deselectAllDataUnits();
        GBRadioButton.setSelected(true);
      }
    });

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
        m.setPoll(getPoll());
        if(grafico != null){
          grafico.setTimeUnits(getTimeUnit());
          grafico.setDataUnits(getDataUnit());
          grafico.setPontos(sliderPontos.getValue());
        }
        refresher.refresh(false);
      }
    });

    this.padraoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        cbPolling.setSelectedItem("30 segundos");
        sliderPontos.setValue(25);

        segundosRadioButton.setSelected(false);
        minutosRadioButton.setSelected(true);
        horasRadioButton.setSelected(false);

        byteRadioButton.setSelected(false);
        kBRadioButton.setSelected(true);
        MBRadioButton.setSelected(false);
        GBRadioButton.setSelected(false);
      }
    });

    this.conectarButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        try{
          m.connect(ipTextField.getText(),Integer.parseInt(portaTextField.getText()));
        } catch (UnknownHostException e) {
          JOptionPane.showMessageDialog(MainFrame.this, "Endereco invalido");
        }
        catch (NumberFormatException e){
          JOptionPane.showMessageDialog(MainFrame.this, "Porta invalida");
        }
      }
    });
  }

  public MainFrame() throws InterruptedException, UnknownHostException {
    this.initMyComponents();

    m = new Monitor(getPoll(),this.sliderPontos.getMaximum(),this);
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
        return 5000;
      case "15 segundos":
        return 15000;
      case "30 segundos":
        return 30000;
      case "1 minuto":
        return 60000;
      case "2 minutos":
        return 120000;
      case "5 minutos":
        return 300000;
      case "15 minutos":
        return 900000;
      case "30 minutos":
        return 1800000;
      case "1 hora":
        return 3600000;
    }
    return 1000;
  }

  public Grafico.TimeUnit getTimeUnit(){
    if(this.segundosRadioButton.isSelected())
      return Grafico.TimeUnit.SECOND;
    else if(this.minutosRadioButton.isSelected())
      return Grafico.TimeUnit.MINUTE;
    else if(this.horasRadioButton.isSelected())
      return Grafico.TimeUnit.HOUR;
    return null;
  }

  public Grafico.DataUnit getDataUnit(){
    if(this.byteRadioButton.isSelected())
      return Grafico.DataUnit.B;
    else if(this.kBRadioButton.isSelected())
      return Grafico.DataUnit.KB;
    else if(this.MBRadioButton.isSelected())
      return Grafico.DataUnit.MB;
    else if(this.GBRadioButton.isSelected())
      return Grafico.DataUnit.GB;
    return null;
  }

  private void deselectAllTimeUnits(){
    this.segundosRadioButton.setSelected(false);
    this.minutosRadioButton.setSelected(false);
    this.horasRadioButton.setSelected(false);
  }

  private void deselectAllDataUnits(){
    this.byteRadioButton.setSelected(false);
    this.kBRadioButton.setSelected(false);
    this.MBRadioButton.setSelected(false);
    this.GBRadioButton.setSelected(false);
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
    this.refresher.setInterfaces(new TreeSet<String>());
    this.setGrafico(null);

  }

  // TODO: Redimensionar componentes com a janela
  public static void main(String[] args) throws InterruptedException, UnknownHostException {
    JFrame frame = new JFrame("Monitor");
    frame.setContentPane(new MainFrame().rootPanel);
    //frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
