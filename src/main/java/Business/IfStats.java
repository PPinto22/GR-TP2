package Business;

import java.sql.Time;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by pedro on 29-10-2016.
 */
public class IfStats {
  private String desc;
  private TreeSet<Ponto> pontosIn;
  private TreeSet<Ponto> pontosOut;
  private Ponto in0;
  private Ponto out0;
  private int maxp;
  private long poll;

  public IfStats(String desc, int maxPontos, long poll){
    this.desc = desc;
    this.pontosIn = new TreeSet<>();
    this.pontosOut = new TreeSet<>();
    this.in0 = null;
    this.out0 = null;
    this.maxp = maxPontos;
    this.poll = poll;
  }

  public String getDesc() {
    return desc;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  public TreeSet<Ponto> getPontosIn() {
    return pontosIn;
  }
  public void setPontosIn(TreeSet<Ponto> pontosIn) {
    this.pontosIn = pontosIn;
  }
  public TreeSet<Ponto> getPontosOut() {
    return pontosOut;
  }
  public void setPontosOut(TreeSet<Ponto> pontosOut) {
    this.pontosOut = pontosOut;
  }
  public Ponto getIn0() {
    return in0;
  }
  public void setIn0(Ponto in0) {
    this.in0 = in0;
  }
  public Ponto getOut0() {
    return out0;
  }
  public void setOut0(Ponto out0) {
    this.out0 = out0;
  }
  public int getMaxp() {
    return maxp;
  }
  public void setMaxp(int maxp) {
    this.maxp = maxp;
  }
  public long getPoll() {
    return poll;
  }
  public void setPoll(long poll) {
    this.poll = poll;
  }

  public synchronized void add(Time sysUpTime, Interface iface){
    Ponto pIn = new Ponto(sysUpTime, iface.getInOctets(), -1, -1);
    Ponto pOut = new Ponto(sysUpTime, iface.getOutOctets(), -1, -1);
    if(this.in0 == null || this.out0 == null) {
      this.in0 = pIn;
      this.out0 = pOut;
    }
    else{
      Ponto prevIn = getPreviousIn(pIn);
      Ponto prevOut = getPreviousOut(pOut);
      Ponto postIn = getPosteriorIn(pIn);
      Ponto postOut = getPosteriorOut(pOut);
      if(postIn == null){
        // Ponto mais recente que todos os existentes
        float elapsedTime = (pIn.getSysUpTime().getTime() - prevIn.getSysUpTime().getTime())/1000.0f;
        pIn.setX(0);
        pIn.setY(pIn.getTotalOct() - prevIn.getTotalOct());
        pOut.setX(0);
        pOut.setY(pOut.getTotalOct() - prevOut.getTotalOct());

        this.shiftAll(elapsedTime);
      }
      else {
        // Ponto intermedio.
        float delta = (postIn.getSysUpTime().getTime() - pIn.getSysUpTime().getTime())/1000.0f;
        // delta = diferenca de tempo entre o novo ponto e o ponto a sua direita
        pIn.setX(postIn.getX() - delta);
        pIn.setY(pIn.getTotalOct() - prevIn.getTotalOct());
        pOut.setX(postOut.getX() - delta);
        pOut.setY(pOut.getTotalOct() - prevOut.getTotalOct());
        postIn.setY(postIn.getTotalOct() - pIn.getTotalOct());
        postOut.setY(postOut.getTotalOct() - pOut.getTotalOct());
      }

      // FIXME Solucao provisoria para quando o contador da uma volta completa
      if(pIn.getY() >= 0)
        this.pontosIn.add(pIn);
      if(pOut.getY() >= 0)
        this.pontosOut.add(pOut);

      if(this.pontosIn.size() > this.maxp){
        this.pontosIn.remove(this.pontosIn.first());
      }
      if(this.pontosOut.size() > this.maxp){
        this.pontosOut.remove(this.pontosOut.first());
      }
    }
  }

  private void shiftAll(float elapsedTime) {
    for(Ponto p: this.pontosOut){
      p.decrementX(elapsedTime);
    }
    for(Ponto p: this.pontosIn){
      p.decrementX(elapsedTime);
    }
  }

  private Ponto getPreviousIn(Ponto p){
    if(this.pontosIn.isEmpty())
      return this.in0;

    Iterator<Ponto> it = this.pontosIn.iterator();
    Ponto prev = null;
    while(it.hasNext() && p.compareTo( (prev = it.next()) ) == 1  );
    return prev;
  }

  private Ponto getPosteriorIn(Ponto p) {
    Iterator<Ponto> it = this.pontosIn.iterator();
    Ponto post = null;
    while(it.hasNext()) {
      if (p.compareTo((post = it.next())) == -1)
        return post;
    }
    return null;
  }

  private Ponto getPreviousOut(Ponto p){
    if(this.pontosOut.isEmpty())
      return this.out0;

    Iterator<Ponto> it = this.pontosOut.iterator();
    Ponto prev = null;
    while(it.hasNext() && p.compareTo( (prev = it.next()) ) == 1  );
    return prev;
  }

  private Ponto getPosteriorOut(Ponto p) {
    Iterator<Ponto> it = this.pontosOut.iterator();
    Ponto post = null;
    while(it.hasNext()) {
      if (p.compareTo((post = it.next())) == -1)
        return post;
    }
    return null;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(desc+"{");
    sb.append("\n\tpontosIn={");
    for(Ponto p: pontosIn){
      sb.append("\n\t\t"+p.toString());
    }
    sb.append("\n\t}");
    sb.append("\n\tpontosOut={");
    for(Ponto p: pontosOut){
      sb.append("\n\t\t"+p.toString());
    }
    sb.append("\n\t}");
    sb.append("\n\tin0 = " + in0.toString());
    sb.append("\n\tout0 = " + out0.toString());
    sb.append("\n\tmaxp = " + maxp);
    sb.append("\n}");
    return sb.toString();
  }

  public double getPollTimeInHours() {
    return this.poll/3600000.0f;
  }

  public double getPollTimeInMinutes() {
    return this.poll/60000.0f;
  }

  public double getPollTimeInSeconds() {
    return this.poll/1000.0f;
  }
}
