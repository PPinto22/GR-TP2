package Business;

import org.snmp4j.CommunityTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by pedro on 26-10-2016.
 */
public class Monitor {

  private SnmpConnector con;

  public Monitor(){
    this.con = null;
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

}
