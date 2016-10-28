package Business;

/**
 * Created by pedro on 27-10-2016.
 */
public class Ponto implements Comparable<Ponto>{

  public float x;
  public float y;

  public Ponto(){
    this.x = 0;
    this.y = 0;
  }

  public Ponto(float x, float y){
    this.x = x;
    this.y = y;
  }

  //TODO: Gerar equals e hashCode quando os tipos estiverem certos

  public int compareTo(Ponto p) {
    if(this.x > p.x)
      return 1;
    else if(this.x < p.x)
      return -1;
    else return 0;
  }
}
