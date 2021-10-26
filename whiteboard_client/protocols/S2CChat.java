/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

package protocols;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class S2CChat implements Msg {
  private String content;

  public S2CChat() {
  }

  public S2CChat(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public byte[] marshal() {
    ByteBuffer buf = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
    buf.put(content.getBytes());
    return Arrays.copyOf(buf.flip().array(), buf.remaining());
  }

  public void unmarshal(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    content = new String(Arrays.copyOfRange(buf.array(), buf.position(), buf.limit()));
  }
}