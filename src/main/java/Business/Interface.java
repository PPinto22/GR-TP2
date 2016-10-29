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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Interface that = (Interface) o;

    if (index != that.index) return false;
    if (inOctets != that.inOctets) return false;
    if (outOctets != that.outOctets) return false;
    return desc != null ? desc.equals(that.desc) : that.desc == null;

  }

  @Override
  public int hashCode() {
    int result = index;
    result = 31 * result + (desc != null ? desc.hashCode() : 0);
    result = 31 * result + inOctets;
    result = 31 * result + outOctets;
    return result;
  }

  public int compareTo(Interface anInterface) {
    if(this.index > anInterface.getIndex())
      return 1;
    else if(this.index < anInterface.getIndex())
      return -1;
    else return 0;
  }

  @Override
  public String toString() {
    return "Interface{" +
        "index=" + index +
        ", desc='" + desc + '\'' +
        ", inOctets=" + inOctets +
        ", outOctets=" + outOctets +
        '}';
  }
}