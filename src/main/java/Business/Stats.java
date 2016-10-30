package Business;

import java.sql.Time;
import java.util.TreeMap;

/**
 * Created by pedro on 29-10-2016.
 */
public class Stats {
  private int pontos;
  private long poll;
  public TreeMap<String,IfStats> ifstats;

  public synchronized int getPontos() {
    return pontos;
  }
  public synchronized void setPontos(int pontos) {
    this.pontos = pontos;
    for(IfStats stats: this.ifstats.values()){
      stats.setMaxp(this.pontos);
    }
  }

  public synchronized long getPoll() {
    return poll;
  }
  public synchronized void setPoll(long poll) {
    this.poll = poll;
    for(IfStats st: this.ifstats.values()){
      st.setPoll(poll);
    }
  }

  public synchronized TreeMap<String, IfStats> getIfstats() {
    return ifstats;
  }
  public synchronized void setIfstats(TreeMap<String, IfStats> ifstats) {
    this.ifstats = ifstats;
  }

  public Stats(int pontos,long poll){
    this.pontos = pontos;
    this.poll = poll;
    this.ifstats = new TreeMap<>();
  }

  public synchronized void add(Interfaces ifaces){
    // Remover interfaces down
    for(String s: this.ifstats.keySet()){
      if(!ifaces.getInterfaces().containsKey(s))
        this.ifstats.remove(s);
    }

    // Adicionar novas interfaces
    for(String s: ifaces.getInterfaces().keySet()){
      if(!this.ifstats.containsKey(s))
        this.ifstats.put(s,new IfStats(s,this.pontos,this.poll));
    }

    // Adicionar pontos
    for (String s: this.ifstats.keySet()) {
      Time sysUpTime = ifaces.getSysUpTime();
      Interface iface = ifaces.getInterfaces().get(s);
      IfStats ifstats = this.ifstats.get(s);
      ifstats.add(sysUpTime,iface);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Stats{");
    sb.append("\npontos = " + pontos);
    sb.append("\nIfStats={");
    for(String desc: ifstats.keySet()){
      sb.append("\n"+desc+"={");
      sb.append("\n"+ifstats.get(desc).toString());
    }
    sb.append("\n}");
    return sb.toString();
  }

}
