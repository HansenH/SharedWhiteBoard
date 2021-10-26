/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.swing.JTextField;

public class Canvas {
  private BufferedImage image;
  private Graphics g;
  private JTextField text = new JTextField();

  public Canvas() {
    image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
    g = image.getGraphics();
    g.setColor(Color.white);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
  }

  public BufferedImage getImage() {
    return image;
  }

  public void clearAll() {
    g.setColor(Color.white);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
  }

  public synchronized void drawPencil(Color c, int[] xPath, int[] yPath, int pathLength) {
    g.setColor(c);
    g.drawPolyline(xPath, yPath, pathLength);
  }

  public synchronized void drawLine(Color c, int x1, int y1, int x2, int y2) {
    g.setColor(c);
    g.drawLine(x1, y1, x2, y2);
  }

  public synchronized void drawRect(Color c, int x1, int y1, int x2, int y2) {
    g.setColor(c);
    g.drawRect(Math.min(x1, x2), Math.min(y1, y2), 
              Math.abs(x1 - x2), Math.abs(y1 - y2));
  }

  public synchronized void drawOval(Color c, int x1, int y1, int x2, int y2) {
    g.setColor(c);
    g.drawOval(Math.min(x1, x2), Math.min(y1, y2), 
              Math.abs(x1 - x2), Math.abs(y1 - y2));
  }

  public synchronized void drawText(Color c, String content, int x, int y) {
    g.setColor(c);
    g.setFont(text.getFont().deriveFont(Font.PLAIN, (float)24));
    g.drawString(content, x, y);
  }

  public synchronized void drawEraser(int x2, int y2) {
    g.setColor(Color.white);
    g.fillOval(x2 - 20, y2 - 20, 40, 40);
  }
}
