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
import java.util.ArrayList;
import java.util.TreeMap;

/**con
 * Created by pedro on 26-10-2016.
 */
public class SnmpConnector {
  static final String COM_STRING = "public";

  private CommunityTarget com;
  private long timeout = 3000;
  private int retries = 3;
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

  public PDU newPDU(){
    PDU pdu = new PDU();
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
    PDU p = newPDU();
    p.add(new VariableBinding(new OID(oid)));
    ResponseEvent re = snmp.get(p,this.com);

    if (re==null){
      throw new IOException("Sem resposta (1)");
    }

    PDU rp = re.getResponse();

    if (rp==null) {
      throw new IOException("Sem resposta (2)");
    }

    if(rp.getErrorStatus() == PDU.noError){
      return rp.getVariable(new OID(oid));
    }
    else{
      throw new IOException("Resposta com erros");
    }
  }

  public Interfaces getInterfaces() throws IOException {
    Snmp snmp = newSNMP();
    PDU p = newPDU();
    p.setType(PDU.GETBULK);
    p.add(new VariableBinding(new OID(Objects.SYSUPTIME)));
    p.add(new VariableBinding(new OID(Objects.IFNUMBER)));
    p.add(new VariableBinding(new OID(Objects.IFDESCR)));
    p.add(new VariableBinding(new OID(Objects.IFOPERSTATUS)));
    p.add(new VariableBinding(new OID(Objects.IFADMINSTATUS)));
    p.add(new VariableBinding(new OID(Objects.IFINOCTETS)));
    p.add(new VariableBinding(new OID(Objects.IFOUTOCTETS)));
    p.setNonRepeaters(2);
    p.setMaxRepetitions(15);

    ResponseEvent re = snmp.getBulk(p,com);

    if (re==null){
      throw new IOException("Sem resposta (1)");
    }

    PDU rp = re.getResponse();

    if (rp==null) {
      throw new IOException("Sem resposta (2)");
    }

    if(rp.getErrorStatus() == PDU.noError){
      Time t = parseTime(rp.getVariable(new OID(Objects.SYSUPTIME + ".0")));
      int number = rp.getVariable(new OID(Objects.IFNUMBER + ".0")).toInt();
      TreeMap<String,Interface> interfaces = new TreeMap<>();
      for(int i = 1; i <= number; i++){
        boolean operStatus = rp.getVariable(new OID(Objects.IFOPERSTATUS + "." + i)).toInt() == 1;
        boolean adminStatus = rp.getVariable(new OID(Objects.IFADMINSTATUS + "." + i)).toInt() == 1;
        boolean up = operStatus && adminStatus;
        if(up){
          int index = i;
          String desc = rp.getVariable(new OID(Objects.IFDESCR + "." + i)).toString();
          int inOct = rp.getVariable(new OID(Objects.IFINOCTETS + "." + i)).toInt();
          int outOct = rp.getVariable(new OID(Objects.IFOUTOCTETS + "." + i)).toInt();

          Interface iface = new Interface(index,desc,inOct,outOct);
          interfaces.put(desc,iface);
        }
      }
      return new Interfaces(t,interfaces);
    }
    else{
      throw new IOException("Resposta com erros");
    }
  }

}
