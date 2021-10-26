/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import protocols.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ClientMain {
  private String serverHost;
  private int serverPort;
  private Socket socket;
  private UIFrame gui;
  private MsgHandler msgHandler;
  private HeartBeat heartBeat;
  private MsgSender msgSender;
  public static final int TIMEOUT = 5000;

  public ClientMain(String host, int port) {
    this.serverHost = host;
    this.serverPort = port;
  }

  public static void main(String args[]) {
    String serverHost = "127.0.0.1";  // default host
    int serverPort = 9012;  // default port
    if (args.length == 2) {
      serverHost = args[0];
      serverPort = Integer.parseInt(args[1]);
    } else if (args.length == 1) {
      serverHost = args[0];
    }
    ClientMain client = new ClientMain(serverHost, serverPort);
    client.boot();
  }

  public void boot() {
    LoginWindow loginWindow = new LoginWindow("Please enter your name:");
    while (true) {
      String username = loginWindow.getUsername();
      loginWindow.dispose();
      if (username.equals("offline")) {  // offline mode
        gui = new UIFrame();
        gui.updateUserList(null);
        return;
      } else {                           // online mode
        int state = login(username);
        if (state == 0) {
          break;  // succeeded
        } else if (state == 1) {
          loginWindow = new LoginWindow("The name has been used.");
        } else if (state == 2) {
          loginWindow = new LoginWindow("Connection Failed!");
        }
      }
    }
    gui = new UIFrame();
    gui.setMsgSender(msgSender);
    msgHandler.setGUI(gui);
    heartBeat = new HeartBeat(msgSender);
    heartBeat.start();
    msgSender.send(new C2SInitCanvas());  // sync canvas from the server
  }

  public void reboot() {
    gui.closeWindow();
    boot();
  }

  public int login(String username) {
    boolean success = connect();
    if (success) {
      try{
        msgSender = new MsgSender(this, socket);
        msgHandler = new MsgHandler(this, socket, msgSender);
        msgHandler.start();
      } catch (IOException | NullPointerException e) {
        return 2;
      }
      msgSender.send(new C2SLogin(username));
      S2CLogin msg = (S2CLogin)(msgHandler.expectResponse("S2CLogin"));
      if (msg.getState() == 0) {
        return 0;
      } else {
        return 1;
      }
    } else {
      return 2;
    }
  }

  public boolean connect() {
    try {
      socket = new Socket();
      socket.setKeepAlive(true);
      socket.connect(new InetSocketAddress(serverHost, serverPort), TIMEOUT);
    } catch (SocketTimeoutException e) {
      return false;
    } catch (IOException e) {
      return false;
    }
    try {
      socket.setSoTimeout(TIMEOUT);
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return true;
  }

  public void disconnect() {
    try {
      socket.close();
      msgHandler.stop();
      heartBeat.stop();
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
    boolean option = gui.lostConnection();
    if (option) {
      reboot();
    } else {
      gui.exit();
    }
  }
}

