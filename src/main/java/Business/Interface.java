package Business;

/**
 * Created by pedro on 29-10-2016.
 */
public class Interface implements Comparable<Interface>{
  public int index;
  public String desc;
  public int inOctets;
  public int outOctets;

  public Interface(int index, String desc, int inOctets, int outOctets) {
    this.index = index;
    this.desc = desc;
    this.inOctets = inOctets;
    this.outOctets = outOctets;
  }

  public int getIndex() {
    return index;
  }
  public void setIndex(int index) {
    this.index = index;
  }
  public String getDesc() {
    return desc;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  public int getInOctets() {
    return inOctets;
  }
  public void setInOctets(int inOctets) {
    this.inOctets = inOctets;
  }
  public int getOutOctets() {
    return outOctets;
  }
  public void setOutOctets(int outOctets) {
    this.outOctets = outOctets;
  }

  // TODO: Gerar equals e hashcode

  public int compareTo(Interface anInterface) {
    if(this.index > anInterface.getIndex())
      return 1;
    else if(this.index < anInterface.getIndex())
      return -1;
    else return 0;
  }
}