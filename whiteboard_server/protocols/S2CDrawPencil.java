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

public class S2CDrawPencil implements Msg {
  private Color color;
  private int[] xPath = new int[10];
  private int[] yPath = new int[10];
  private int pathLength;

  public S2CDrawPencil() {
  }

  public S2CDrawPencil(Color color, int[] xPath, int[] yPath, int pathLength) {
    this.color = color;
    this.xPath = xPath;
    this.yPath = yPath;
    this.pathLength = pathLength;
  }

  public Color getColor() {
    return color;
  }

  public int[] getXPath() {
    return xPath;
  }

  public int[] getYPath() {
    return yPath;
  }

  public int getPathLength() {
    return pathLength;
  }

  public byte[] marshal() {
    ByteBuffer buf = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
    buf.putInt(color.getRGB());
    for (int i = 0; i < 10; ++i) {
      buf.putInt(xPath[i]);
      buf.putInt(yPath[i]);
    }
    buf.putInt(pathLength);
    return Arrays.copyOf(buf.flip().array(), buf.remaining());
  }

  public void unmarshal(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    color = new Color(buf.getInt());
    for (int i = 0; i < 10; ++i) {
      xPath[i] = buf.getInt();
      yPath[i] = buf.getInt();
    }
    pathLength = buf.getInt();
  }
}