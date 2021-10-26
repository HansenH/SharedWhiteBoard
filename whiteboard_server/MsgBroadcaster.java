/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import protocols.*;
import java.util.ArrayList;
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

public class MsgBroadcaster {
  private ArrayList<Socket> socketList = new ArrayList<>();
  private ArrayList<OutputStream> outputStreamList = new ArrayList<>();
  private ArrayList<String> usernameList = new ArrayList<>();
  
  public MsgBroadcaster() {
  }

  public void addClient(Socket socket, String username) {
    try {
      socketList.add(socket);
      outputStreamList.add(socket.getOutputStream());
      usernameList.add(username);
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  public void removeClient(Socket socket) {
    int i = socketList.indexOf(socket);
    socketList.remove(i);
    outputStreamList.remove(i);
    usernameList.remove(i);
    try {
      socket.close();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace ();
    }
  }

  public boolean isUniqueName(String username) {
    if (usernameList.contains(username)) {
      return false;
    } else {
      return true;
    }
  }

  public String printUserList() {
    String userList = "";
    for (int i = 0; i < usernameList.size(); ++i) {
      userList += usernameList.get(i);
      userList += "\n";
    }
    return userList;
  }

  public void sendResponse(Msg msg, Socket socket) {
    try{
      int i = socketList.indexOf(socket);
      OutputStream outS = outputStreamList.get(i);
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
    }
  }

  public void sendToAll(Msg msg) {
    try{
      for (int i = 0; i < outputStreamList.size(); ++i) {
        OutputStream outS = outputStreamList.get(i);
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
      }
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }
}