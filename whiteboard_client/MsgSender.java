/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import protocols.*;
import java.net.Socket;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*
  protocol definition (BIG_ENDIAN)

  int(4B)    msgId
  int(4B)    len (length of byte[] of msg)
  byte[len]  Msg body
  
*/

public class MsgSender {
  private Socket socket;
  private OutputStream outS;
  private ClientMain client;
  
  public MsgSender(ClientMain client, Socket socket) throws IOException, NullPointerException {
    this.client = client;
    this.socket = socket;
    this.outS = socket.getOutputStream();
  }

  public void send(Msg msg) {
    try{
      byte[] msgBytes = msg.marshal();
      int msgId = MsgId.getMsgId(msg.getClass().getSimpleName());
      ByteBuffer buf = ByteBuffer.allocate(8 + msgBytes.length).order(ByteOrder.BIG_ENDIAN);
      buf.putInt(msgId);
      buf.putInt(msgBytes.length);
      buf.put(msgBytes);
      byte[] outBytes = buf.array();
      outS.write(outBytes);
      outS.flush();
      System.out.println("Send: " + msgId);
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
      client.disconnect();
    }
  }
}