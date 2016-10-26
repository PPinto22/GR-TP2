import Business.Monitor;

import java.net.UnknownHostException;

/**
 * Created by pedro on 26-10-2016.
 */
public class Teste1 {

  public static void main(String[] args) throws UnknownHostException {
    Monitor m = new Monitor();
    m.connect("localhost",5555);

  }
}
