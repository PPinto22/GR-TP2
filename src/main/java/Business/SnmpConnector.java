package Business;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.List;

/**
 * Created by pedro on 26-10-2016.
 */
public class SnmpConnector {
  static final String COM_STRING = "public";

  private CommunityTarget com;
  private long timeout = 3000;
  private int retries = 0;
  private int version = SnmpConstants.version2c;
  private int requestID = 0;

  public SnmpConnector(String ip, int port) throws UnknownHostException {
    this.connect(ip,port);
  }

  public static Snmp newSNMP() throws IOException {
    TransportMapping transport = new DefaultUdpTransportMapping();
    transport.listen();
    return new Snmp(transport);
  }

  public PDU newPDU(List<String> OIDs, int tipo){
    PDU newpdu = new PDU();
    newpdu.add(new VariableBinding(new OID(Objects.SYSUPTIME))); //add systime to pdu
    for(String OID : OIDs){
      newpdu.add(new VariableBinding(new OID(OID)));
    }
    newpdu.setType(tipo);
    newpdu.setRequestID(new Integer32(this.requestID++));
    return newpdu;
  }

  public PDU newPDU(String oid){
    PDU pdu = new PDU();
    pdu.add(new VariableBinding(new OID(oid)));
    pdu.setRequestID(new Integer32(this.requestID++));
    return pdu;
  }

  public void connect(String ip, int port) throws UnknownHostException {
    this.com = new CommunityTarget(
        new UdpAddress(InetAddress.getByName(ip),port),
        new OctetString(COM_STRING)
    );
    this.com.setTimeout(this.timeout);
    this.com.setRetries(this.retries);
    this.com.setVersion(this.version);
  }

  public Time parseTime(Variable time){
    String ts = time.toString();
    ts = ts.substring(0,ts.indexOf('.'));
    return Time.valueOf(ts);
  }

  public Variable get(String oid) throws IOException {
    Snmp snmp = newSNMP();
    ResponseEvent re = snmp.get(newPDU(oid),this.com);

    if (re==null){
      throw new IOException("Sem resposta (1)");
    }

    PDU rpdu = re.getResponse();

    if (rpdu==null) {
      throw new IOException("Sem resposta (2)");
    }

    if(rpdu.getErrorStatus() == PDU.noError){
      return rpdu.getVariable(new OID(oid));
    }
    else{
      throw new IOException("Resposta com erros");
    }

  }


}
