/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

package protocols;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class MsgId {
  private static final HashMap<String, Integer> MSG_TO_ID = new HashMap<>();
  private static final HashMap<Integer, String> ID_TO_MSG = new HashMap<>();
  static {
    String filename = "protocols/msgid.csv";
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
      String line = null;
      while ((line = br.readLine()) != null) {
        String[] lineSlpit = line.split(",");
        String msgName = lineSlpit[0];
        int msgId = Integer.parseInt(lineSlpit[1]);
        MSG_TO_ID.put(msgName, msgId);
        ID_TO_MSG.put(msgId, msgName);
      }
      br.close();
    } 
    catch (FileNotFoundException e) {
      System.out.println("Cannot find msgid.csv");
    }
    catch (IOException e) {
      System.out.println("IOException!");
    }
  }

  public static int getMsgId(String msgName) {
    return MSG_TO_ID.getOrDefault(msgName, 0);
  }
  
  public static String getMsgName(int msgId) {
    return ID_TO_MSG.get(msgId);
  }
}