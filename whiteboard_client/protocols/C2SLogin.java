/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

package protocols;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class C2SLogin implements Msg {
  private String username;

  public C2SLogin() {
  }

  public C2SLogin(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public byte[] marshal() {
    ByteBuffer buf = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
    buf.put(username.getBytes());
    return Arrays.copyOf(buf.flip().array(), buf.remaining());
  }

  public void unmarshal(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    username = new String(Arrays.copyOfRange(buf.array(), buf.position(), buf.limit()));
  }
}