/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

package protocols;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class S2CPong implements Msg {
  private String userList = "";

  public S2CPong() {
  }

  public S2CPong(String userList) {
    this.userList = userList;
  }

  public String getUserList() {
    return userList;
  }

  public byte[] marshal() {
    ByteBuffer buf = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
    buf.put(userList.getBytes());
    return Arrays.copyOf(buf.flip().array(), buf.remaining());
  }

  public void unmarshal(byte[] bytes) {
    ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    userList = new String(Arrays.copyOfRange(buf.array(), buf.position(), buf.limit()));
  }
}