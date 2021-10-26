/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

package protocols;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class S2CLogin implements Msg {
  private int state;

  public S2CLogin() {
  }

  public S2CLogin(int state) {
    this.state = state;
  }

  public int getState() {
    return state;
  }

  public byte[] marshal() {
    ByteBuffer buf = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
    buf.putInt(state);
    return Arrays.copyOf(buf.flip().array(), buf.remaining());
  }

  public void unmarshal(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    state = buf.getInt();
  }
}