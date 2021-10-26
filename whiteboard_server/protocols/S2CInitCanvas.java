/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

package protocols;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class S2CInitCanvas implements Msg {
  private BufferedImage image;

  public S2CInitCanvas() {
  }

  public S2CInitCanvas(BufferedImage image) {
    this.image = image;
  }

  public BufferedImage getImage() {
    return image;
  }

  public byte[] marshal() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "png", baos);
      byte[] bytes = baos.toByteArray();
      return bytes;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void unmarshal(byte[] bytes) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      image = ImageIO.read(bais);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
