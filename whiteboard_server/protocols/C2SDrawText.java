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

public class C2SDrawText implements Msg {
  private Color color;
  private String content;
  private int x;
  private int y;

  public C2SDrawText() {
  }

  public C2SDrawText(Color color, String content, int x, int y) {
    this.color = color;
    this.content = content;
    this.x = x;
    this.y = y;
  }

  public Color getColor() {
    return color;
  }

  public String getContent() {
    return content;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public byte[] marshal() {
    ByteBuffer buf = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
    buf.putInt(color.getRGB());
    buf.putInt(x);
    buf.putInt(y);
    buf.put(content.getBytes());
    return Arrays.copyOf(buf.flip().array(), buf.remaining());
  }

  public void unmarshal(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    color = new Color(buf.getInt());
    x = buf.getInt();
    y = buf.getInt();
    content = new String(Arrays.copyOfRange(buf.array(), buf.position(), buf.limit()));
  }
}