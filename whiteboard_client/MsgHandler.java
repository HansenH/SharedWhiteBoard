/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import protocols.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CountDownLatch;

/*
  protocol definition (BIG_ENDIAN)

  int(4B)    msgId
  int(4B)    len (length of byte[] of msg)
  byte[len]  Msg body
  
*/

public class MsgHandler extends Thread {
  private ClientMain client;
  private Socket socket;
  private UIFrame gui;
  private MsgSender msgSender;
  private InputStream inS;
  private int expectedMsgId = 0;
  private Msg expectedMsg;
  private CountDownLatch cdLatch;

  public MsgHandler(ClientMain client, Socket socket, MsgSender msgSender) {
    this.client = client;
    this.socket = socket;
    this.msgSender = msgSender;
  }

  public void setGUI(UIFrame gui) {
    this.gui = gui;
  }

  public void run() {
    try {
      inS = socket.getInputStream();
      while (true) {
        byte[] inBytes = new byte[131072];
        inS.read(inBytes);
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
      e.printStackTrace();
      client.disconnect();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
      client.disconnect();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
      client.disconnect();
    }
  }

  // handle protocols asynchronously
  private void handle(int msgId, Msg msg) {
    System.out.println("Recv: " + msgId);
    if (msgId == expectedMsgId) {
      expectedMsg = msg;
      cdLatch.countDown();
    }
    switch (msgId) {
      case 2001:
        handleS2CLogin((S2CLogin)msg);
        break;
      case 2002:
        handleS2CPong((S2CPong)msg);
        break;
      case 2003:
        handleS2CChat((S2CChat)msg);
        break;
      case 2004:
        handleS2CInitCanvas((S2CInitCanvas)msg);
        break;
      case 2005:
        handleS2CClearCanvas((S2CClearCanvas)msg);
        break;
      case 2006:
        handleS2CDrawPencil((S2CDrawPencil)msg);
        break;
      case 2007:
        handleS2CDrawLine((S2CDrawLine)msg);
        break;
      case 2008:
        handleS2CDrawRect((S2CDrawRect)msg);
        break;
      case 2009:
        handleS2CDrawOval((S2CDrawOval)msg);
        break;
      case 2010:
        handleS2CDrawText((S2CDrawText)msg);
        break;
      case 2011:
        handleS2CDrawEraser((S2CDrawEraser)msg);
        break;
    }
  }

  // block to wait for the certain protocol synchronously
  public Msg expectResponse(String msgName) {
    cdLatch = new CountDownLatch(1);
    expectedMsgId = MsgId.getMsgId(msgName);
    try {
      cdLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    expectedMsgId = 0;
    return expectedMsg;
  }

  private void handleS2CLogin(S2CLogin msg) {
    if (msg.getState() == 0) {
      System.out.println("Login succeeded.");
    }
  }

  private void handleS2CPong(S2CPong msg) {
    gui.updateUserList(msg.getUserList());
  }

  private void handleS2CChat(S2CChat msg) {
    String text = String.format("%s\n\n", msg.getContent());
    gui.updateChat(text);
  }

  private void handleS2CInitCanvas(S2CInitCanvas msg) {
    gui.initCanvas(msg.getImage());
  }

  private void handleS2CClearCanvas(S2CClearCanvas msg) {
    gui.clearCanvas();
  }

  private void handleS2CDrawPencil(S2CDrawPencil msg) {
    gui.drawPencil(msg.getColor(), msg.getXPath(), msg.getYPath(), msg.getPathLength());
  }

  private void handleS2CDrawLine(S2CDrawLine msg) {
    gui.drawLine(msg.getColor(), msg.getX1(), msg.getY1(), msg.getX2(), msg.getY2());
  }

  private void handleS2CDrawRect(S2CDrawRect msg) {
    gui.drawRect(msg.getColor(), msg.getX1(), msg.getY1(), msg.getX2(), msg.getY2());
  }

  private void handleS2CDrawOval(S2CDrawOval msg) {
    gui.drawOval(msg.getColor(), msg.getX1(), msg.getY1(), msg.getX2(), msg.getY2());
  }

  private void handleS2CDrawText(S2CDrawText msg) {
    gui.drawText(msg.getColor(), msg.getContent(), msg.getX(), msg.getY());
  }

  private void handleS2CDrawEraser(S2CDrawEraser msg) {
    gui.drawEraser(msg.getX(), msg.getY());
  }
}