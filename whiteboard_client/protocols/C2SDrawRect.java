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

public class C2SDrawRect implements Msg {
  private Color color;
  private int x1;
  private int y1;
  private int x2;
  private int y2;

  public C2SDrawRect() {
  }

  public C2SDrawRect(Color color, int x1, int y1, int x2, int y2) {
    this.color = color;
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }

  public Color getColor() {
    return color;
  }

  public int getX1() {
    return x1;
  }

  public int getY1() {
    return y1;
  }

  public int getX2() {
    return x2;
  }

  public int getY2() {
    return y2;
  }

  public byte[] marshal() {
    ByteBuffer buf = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
    buf.putInt(color.getRGB());
    buf.putInt(x1);
    buf.putInt(y1);
    buf.putInt(x2);
    buf.putInt(y2);
    return Arrays.copyOf(buf.flip().array(), buf.remaining());
  }

  public void unmarshal(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    color = new Color(buf.getInt());
    x1 = buf.getInt();
    y1 = buf.getInt();
    x2 = buf.getInt();
    y2 = buf.getInt();
  }
}