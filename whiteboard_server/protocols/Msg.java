/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

package protocols;

public interface Msg {
  public byte[] marshal();
  public void unmarshal(byte[] bytes);
}