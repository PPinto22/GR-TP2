package Business;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pedro on 29-10-2016.
 */
public class Interfaces {
  private Time sysUpTime;
  private Map<String,Interface> interfaces;

  public Interfaces(Time sysUpTime) {
    this.sysUpTime = sysUpTime;
    this.interfaces = new TreeMap<String,Interface>();
  }

  public Interfaces(Time sysUpTime, Map<String,Interface> interfaces) {
    this.sysUpTime = sysUpTime;
    this.interfaces = interfaces;
  }

  public Time getSysUpTime() {
    return sysUpTime;
  }

  public void setSysUpTime(Time sysUpTime) {
    this.sysUpTime = sysUpTime;
  }

  public Map<String, Interface> getInterfaces() {
    return interfaces;
  }

  public void setInterfaces(Map<String, Interface> interfaces) {
    this.interfaces = interfaces;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("sysUpTime: " + sysUpTime.toString());
    for(Interface iface: this.interfaces.values()) {
      sb.append("\n" + iface.toString());
    }
    return sb.toString();
  }
}
