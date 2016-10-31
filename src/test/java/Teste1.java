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

/*    Time t1 = Time.valueOf("00:10:05");
    Time t2 = Time.valueOf("00:10:35");
    Time t3 = Time.valueOf("00:11:05");
    Time t4 = Time.valueOf("00:11:35");
    Time t5 = Time.valueOf("00:12:05");
    Ponto p1 = new Ponto(t1,1000,0,-1);
    Ponto p2 = new Ponto(t2,2500,-0.5f,1500);
    Ponto p3 = new Ponto(t3,4500,-1.0f,2000);
    Ponto p4 = new Ponto(t4,7000,-1.5f,2500);
    Ponto p5 = new Ponto(t5,10000,-2.0f,3000);*/

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
