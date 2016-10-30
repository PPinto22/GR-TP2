package Business;

import org.snmp4j.CommunityTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TreeMap;

/**
 * Created by pedro on 26-10-2016.
 */
public class Monitor {

  private SnmpConnector con;
  private long poll; //Intervalo em ms entre mensagens de polling
  public Polling pollThread;
  private Stats stats;
  private int maxPoints;

  public Monitor(){
    this.con = null;
    this.poll = 30000;
    this.pollThread = null;
    this.maxPoints = 20;
    this.stats = new Stats(maxPoints,poll);
  }

  public int getMaxPoints() {
    return maxPoints;
  }

  public void setMaxPoints(int maxPoints) {
    this.maxPoints = maxPoints;
    this.stats.setPontos(maxPoints);
  }

  public TreeMap<String,IfStats> getIfStats(){
    return this.stats.getIfstats();
  }

  public void addStats(Interfaces ifaces){
    this.stats.add(ifaces);
  }

  public void startPolling(){
    if(this.pollThread == null) {
      this.pollThread = new Polling(this, this.poll);
      this.pollThread.start();
    }
  }

  public void connect(String ip, int port) throws UnknownHostException {
    this.con = new SnmpConnector(ip,port);
  }

  public boolean isConnected(){ return this.con != null; }

  public Interfaces getInterfaces() throws IOException {
    if(this.con != null)
      return this.con.getInterfaces();
    else throw new IOException("Endereco do agente nao especificado");
  }

  public long getPoll() {
    return poll;
  }

  public void setPoll(long poll) {
    this.poll = poll;
    if(this.pollThread != null){
      this.pollThread.setIntervalo(poll);
    }
    this.stats.setPoll(poll);
  }
}
