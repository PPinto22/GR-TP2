import Business.Interfaces;
import Business.Monitor;
import Business.Objects;
import Business.SnmpConnector;
import org.snmp4j.smi.Variable;

import java.net.UnknownHostException;
import java.sql.Time;

/**
 * Created by pedro on 26-10-2016.
 */
public class Teste1 {

  public static void main(String[] args) throws Exception {
    Monitor m = new Monitor();
    m.connect("localhost",5555);
    Interfaces interfaces = m.getInterfaces();
    System.out.println(interfaces.toString());

  }
}
