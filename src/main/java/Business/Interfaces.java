package Business;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by pedro on 29-10-2016.
 */
public class Interfaces {
  public Time sysUpTime;
  public ArrayList<Interface> interfaces;

  public Interfaces(Time sysUpTime) {
    this.sysUpTime = sysUpTime;
    this.interfaces = new ArrayList<Interface>();
  }

  public Interfaces(Time sysUpTime, ArrayList<Interface> interfaces) {
    this.sysUpTime = sysUpTime;
    this.interfaces = interfaces;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("sysUpTime: " + sysUpTime.toString());
    for(Interface iface: this.interfaces) {
      sb.append("\n" + iface.toString());
    }
    return sb.toString();
  }
}
