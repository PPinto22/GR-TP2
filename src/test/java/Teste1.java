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
/*    Monitor m = new Monitor();
    m.connect("localhost",5555);*/
    SnmpConnector con = new SnmpConnector("localhost",5555);
    System.out.println(con.parseTime(con.get(Objects.SYSUPTIME)).toString());

  }
}
