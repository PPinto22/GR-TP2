package Business;

import java.sql.Time;
import java.util.Date;

/**
 * Created by pedro on 27-10-2016.
 */
public class Ponto implements Comparable<Ponto>{

  private Time sysUpTime;
  private int totalOct;

  private float x; //Tempo (segundos)
  private float y;

  public Time getSysUpTime() {
    return sysUpTime;
  }
  public void setSysUpTime(Time sysUpTime) {
    this.sysUpTime = sysUpTime;
  }
  public int getTotalOct() {
    return totalOct;
  }
  public void setTotalOct(int totalOct) {
    this.totalOct = totalOct;
  }
  public float getX() {
    return x;
  }
  public void setX(float x) {
    this.x = x;
  }
  public float getY() {
    return y;
  }
  public void setY(float y) {
    this.y = y;
  }

  public Ponto(Time sysUpTime, int totalOct, float x, float y){
    this.sysUpTime = sysUpTime;
    this.totalOct = totalOct;
    this.x = x;
    this.y = y;
  }

  public void decrementX(float value){
    this.x -= value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Ponto ponto = (Ponto) o;

    if (totalOct != ponto.totalOct) return false;
    if (Float.compare(ponto.x, x) != 0) return false;
    if (Float.compare(ponto.y, y) != 0) return false;
    return sysUpTime.equals(ponto.sysUpTime);
  }

  @Override
  public int hashCode() {
    int result = sysUpTime.hashCode();
    result = 31 * result + totalOct;
    result = 31 * result + (x != +0.0f ? Float.floatToIntBits(x) : 0);
    result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
    return result;
  }

  public int compareTo(Ponto other) {
    return this.sysUpTime.compareTo(other.getSysUpTime());
  }

  @Override
  public String toString() {
    return "Ponto{" +
        "sysUpTime=" + sysUpTime +
        ", totalOct=" + totalOct +
        ", x=" + x +
        ", y=" + y +
        '}';
  }
}
