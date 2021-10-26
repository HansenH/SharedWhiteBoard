/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

package protocols;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class C2SClearCanvas implements Msg {
  public C2SClearCanvas() {
  }

  public byte[] marshal() {
    ByteBuffer buf = ByteBuffer.allocate(1).order(ByteOrder.BIG_ENDIAN);
    buf.put((byte)0);
    return buf.array();
  }

  public void unmarshal(byte[] bytes) {
  }
}