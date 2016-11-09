import Business.*;
import org.snmp4j.smi.Variable;

import java.net.UnknownHostException;
import java.sql.Time;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by pedro on 26-10-2016.
 */
public class Teste1 {

  public static void main(String[] args) throws Exception {

    Monitor m = new Monitor(15000,60);
    m.connect("localhost",5555);
    while(true){
      Map<String,IfStats> stats = m.getIfStats();
      for(String desc: stats.keySet()){
        if(desc.equals("lo") || desc.equals("wlp58s0")) {
          IfStats st = stats.get(desc);
          System.out.println(st.toString());
        }
      }
      Thread.sleep(15000);
    }
  }
}
