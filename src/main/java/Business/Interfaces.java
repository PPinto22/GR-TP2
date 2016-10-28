package Business;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by pedro on 29-10-2016.
 */
public class Interfaces {
  public Time time;
  public ArrayList<Interface> interfaces;

  public Interfaces(Time time) {
    this.time = time;
    this.interfaces = new ArrayList<Interface>();
  }

  public Interfaces(Time time, ArrayList<Interface> interfaces) {
    this.time = time;
    this.interfaces = interfaces;
  }
}
