/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class ServerMain {
  public static void main(String args[]) {
    int port = 9012;  // default port

    if (args.length == 1) {
      port = Integer.parseInt(args[0]);
      System.out.println("port: " + port);
    } else {
      System.out.println("port: " + port + " (default)");
    }

    try {
      Canvas canvas = new Canvas();
      ServerSocket ss = new ServerSocket(port);
      MsgBroadcaster msgBroadcaster = new MsgBroadcaster();
      while (true) {
        try {
          Socket socket = ss.accept();
          socket.setKeepAlive(true);
          new MsgHandler(socket, canvas, msgBroadcaster).start();  // thread per socket
        } catch (IOException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}