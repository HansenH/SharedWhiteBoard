/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import protocols.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*
  protocol definition (BIG_ENDIAN)

  int(4B)    msgId
  int(4B)    len (length of byte[] of msg)
  byte[len]  Msg body
  
*/

public class MsgHandler extends Thread {
  private Socket socket;
  private Canvas canvas;
  private String username;
  private MsgBroadcaster msgBroadcaster;
  private InputStream inS;
  public static final int TIMEOUT = 5000;

  public MsgHandler(Socket socket, Canvas canvas, MsgBroadcaster msgBroadcaster) {
    this.socket = socket;
    this.canvas = canvas;
    this.msgBroadcaster = msgBroadcaster;
  }

  public void run() {
    try {
      socket.setSoTimeout(TIMEOUT);
      inS = socket.getInputStream();
      while (true) {
        byte[] inBytes = new byte[1024];
        int n = inS.read(inBytes);
        ByteBuffer buf = ByteBuffer.wrap(inBytes).order(ByteOrder.BIG_ENDIAN);
        int msgId = buf.getInt();
        int msgLength = buf.getInt();
        byte[] msgBytes = new byte[msgLength];
        buf.get(msgBytes);
        Msg msg = (Msg)(Class.forName("protocols." + MsgId.getMsgName(msgId)).newInstance());
        msg.unmarshal(msgBytes);
        handle(msgId, msg);
      }
    } catch (SocketTimeoutException e) {
      msgBroadcaster.removeClient(socket);
    } catch (IOException e) {
      msgBroadcaster.removeClient(socket);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      msgBroadcaster.removeClient(socket);
    }
  }

  // handle protocols asynchronously
  private void handle(int msgId, Msg msg) {
    System.out.println("Recv: " + msgId);
    switch (msgId) {
      case 1001:
        handleC2SLogin((C2SLogin)msg);
        break;
      case 1002:
        handleC2SPing((C2SPing)msg);
        break;
      case 1003:
        handleC2SChat((C2SChat)msg);
        break;
      case 1004:
        handleC2SInitCanvas((C2SInitCanvas)msg);
        break;
      case 1005:
        handleC2SClearCanvas((C2SClearCanvas)msg);
        break;
      case 1006:
        handleC2SDrawPencil((C2SDrawPencil)msg);
        break;
      case 1007:
        handleC2SDrawLine((C2SDrawLine)msg);
        break;
      case 1008:
        handleC2SDrawRect((C2SDrawRect)msg);
        break;
      case 1009:
        handleC2SDrawOval((C2SDrawOval)msg);
        break;
      case 1010:
        handleC2SDrawText((C2SDrawText)msg);
        break;
      case 1011:
        handleC2SDrawEraser((C2SDrawEraser)msg);
        break;
    }
  }

  private void handleC2SLogin(C2SLogin msg) {
    if (msgBroadcaster.isUniqueName(msg.getUsername())) {
      username = msg.getUsername();
      msgBroadcaster.addClient(socket, username);
      msgBroadcaster.sendResponse(new S2CLogin(0), socket);
    } else {
      msgBroadcaster.addClient(socket, null);
      msgBroadcaster.sendResponse(new S2CLogin(1), socket);
      msgBroadcaster.removeClient(socket);
    }
  }

  private void handleC2SPing(C2SPing msg) {
    msgBroadcaster.sendResponse(new S2CPong(msgBroadcaster.printUserList()), socket);
  }

  private void handleC2SChat(C2SChat msg) {
    String text = String.format("%s\n%s", username, msg.getContent());
    msgBroadcaster.sendToAll(new S2CChat(text));
  }

  private void handleC2SInitCanvas(C2SInitCanvas msg) {
    msgBroadcaster.sendResponse(new S2CInitCanvas(canvas.getImage()), socket);
  }

  private void handleC2SClearCanvas(C2SClearCanvas msg) {
    msgBroadcaster.sendToAll(new S2CClearCanvas());
    canvas.clearAll();
  }

  private void handleC2SDrawPencil(C2SDrawPencil msg) {
    msgBroadcaster.sendToAll(new S2CDrawPencil(
        msg.getColor(), msg.getXPath(), msg.getYPath(), msg.getPathLength()));
    canvas.drawPencil(msg.getColor(), msg.getXPath(), msg.getYPath(), msg.getPathLength());
  }

  private void handleC2SDrawLine(C2SDrawLine msg) {
    msgBroadcaster.sendToAll(new S2CDrawLine(
        msg.getColor(), msg.getX1(), msg.getY1(), msg.getX2(), msg.getY2()));
    canvas.drawLine(msg.getColor(), msg.getX1(), msg.getY1(), msg.getX2(), msg.getY2());
  }

  private void handleC2SDrawRect(C2SDrawRect msg) {
    msgBroadcaster.sendToAll(new S2CDrawRect(
        msg.getColor(), msg.getX1(), msg.getY1(), msg.getX2(), msg.getY2()));
    canvas.drawRect(msg.getColor(), msg.getX1(), msg.getY1(), msg.getX2(), msg.getY2());
  }

  private void handleC2SDrawOval(C2SDrawOval msg) {
    msgBroadcaster.sendToAll(new S2CDrawOval(
        msg.getColor(), msg.getX1(), msg.getY1(), msg.getX2(), msg.getY2()));
    canvas.drawOval(msg.getColor(), msg.getX1(), msg.getY1(), msg.getX2(), msg.getY2());
  }

  private void handleC2SDrawText(C2SDrawText msg) {
    msgBroadcaster.sendToAll(new S2CDrawText(
        msg.getColor(), msg.getContent(), msg.getX(), msg.getY()));
    canvas.drawText(msg.getColor(), msg.getContent(), msg.getX(), msg.getY());
  }

  private void handleC2SDrawEraser(C2SDrawEraser msg) {
    msgBroadcaster.sendToAll(new S2CDrawEraser(msg.getX(), msg.getY()));
    canvas.drawEraser(msg.getX(), msg.getY());
  }
}
