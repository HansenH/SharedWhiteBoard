/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import protocols.C2SPing;

public class HeartBeat extends Thread {
  private MsgSender msgSender;

  public HeartBeat(MsgSender msgSender) {
    this.msgSender = msgSender;
  }

  public void run() {
    while (true) {
      msgSender.send(new C2SPing());
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
