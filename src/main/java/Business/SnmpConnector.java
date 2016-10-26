package Business;

import org.snmp4j.CommunityTarget;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by pedro on 26-10-2016.
 */
public class SnmpConnector {
  static final String COM_STRING = "public";

  private CommunityTarget com;
  private long timeout = 3000;
  private int retries = 3;
  private int versao = SnmpConstants.version2c;

  public SnmpConnector(String ip, int port) throws UnknownHostException {
    this.connect(ip,port);
  }

  public void connect(String ip, int port) throws UnknownHostException {
    this.com = new CommunityTarget(
        new UdpAddress(InetAddress.getByName(ip),port),
        new OctetString(COM_STRING)
    );
    this.com.setTimeout(this.timeout);
    this.com.setRetries(this.retries);
    this.com.setVersion(SnmpConstants.version2c);
  }

  public void get(String oid){

  }


}
