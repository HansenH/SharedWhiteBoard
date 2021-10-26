/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

package protocols;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.awt.Color;

public class S2CDrawEraser implements Msg {
  private int x;
  private int y;

  public S2CDrawEraser() {
  }

  public S2CDrawEraser(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public byte[] marshal() {
    ByteBuffer buf = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
    buf.putInt(x);
    buf.putInt(y);
    return Arrays.copyOf(buf.flip().array(), buf.remaining());
  }

  public void unmarshal(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    x = buf.getInt();
    y = buf.getInt();
  }
}