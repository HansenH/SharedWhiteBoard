/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import protocols.*;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
  private int x1 = 0;
  private int y1 = 0;
  private int x2 = 0;
  private int y2 = 0;
  private int[] xPath = new int[10];  // for pencil
  private int[] yPath = new int[10];
  private int pathLength = 0;
  private BufferedImage image;
  private int toolType;
  private JTextField textBox;
  private boolean idle = true;
  private MsgSender msgSender;

  public Canvas() {
    setLayout(null);
    image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    g.setColor(Color.white);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    textBox = new JTextField();
    textBox.setFont(textBox.getFont().deriveFont(Font.PLAIN, (float)24));
    textBox.setEditable(true);
    textBox.setOpaque(false);
    textBox.setBorder(BorderFactory.createLineBorder​(new Color(240, 240, 240), 1));
    textBox.setLocation(0, 0);
    textBox.setSize(1920, 32);
  }

  public void setMsgSender(MsgSender msgSender) {
    this.msgSender = msgSender;
  }

  public BufferedImage getImage() {
    return image;
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }

  public void setIdle(boolean state) {
    this.idle = state;
  }

  public void setColor(Color color) {
    if (this.toolType == 5) {
      this.draw();
      this.remove(textBox);
      this.repaint();
    }
    this.setForeground(color);
  }

  public void setToolType(int toolType) {
    if (this.toolType == 5) {
      this.draw();
      this.remove(textBox);
      this.repaint();
    }
    this.toolType = toolType;
  }

  public void setPoint1(int x1, int y1) {
    if (toolType == 1) {
      // for pencil (polyline's path)
      pathLength = 0;
      return;
    }
    this.x1 = x1;
    this.y1 = y1;
  }

  public void setPoint2(int x2, int y2) {
    if (toolType == 1) {
      // for pencil (polyline's path)
      this.xPath[pathLength] = x2;
      this.yPath[pathLength] = y2;
      ++pathLength;
      if (pathLength == 10) {
        this.draw();
        this.xPath[0] = x2;  // last point as the start of the next polyline
        this.yPath[0] = y2;
        pathLength = 1;
      }
      return;
    }
    this.x2 = x2;
    this.y2 = y2;
  }

  public void clearAll() {
    Graphics g = image.getGraphics();
    g.setColor(Color.white);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    textBox.setText("");
    this.remove(textBox);
  }

  // to display the preview before really drawing onto the buffered image
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, null);
    g.setColor(this.getForeground());
    if (this.idle) return;
    switch (toolType) {
      case 1:
        this.drawPencil(g, xPath, yPath, pathLength);
        break;
      case 2:
        this.drawLine(g, x1, y1, x2, y2);
        break;
      case 3:
        this.drawRect(g, x1, y1, x2, y2);
        break;
      case 4:
        this.drawOval(g, x1, y1, x2, y2);
        break;
      case 6:
        this.draw();
        break;
    }
  }

  // Draw new elements onto the buffered image and send network messages
  public void draw() {
    Graphics g = image.getGraphics();
    g.setColor(this.getForeground());
    switch (toolType) {
      case 1:
        this.drawPencil(g, xPath, yPath, pathLength);
        if (msgSender != null) {
          msgSender.send(new C2SDrawPencil(g.getColor(), xPath, yPath, pathLength));
        }
        break;
      case 2:
        this.drawLine(g, x1, y1, x2, y2);
        if (msgSender != null) {
          msgSender.send(new C2SDrawLine(g.getColor(), x1, y1, x2, y2));
        }
        break;
      case 3:
        this.drawRect(g, x1, y1, x2, y2);
        if (msgSender != null) {
          msgSender.send(new C2SDrawRect(g.getColor(), x1, y1, x2, y2));
        }
        break;
      case 4:
        this.drawOval(g, x1, y1, x2, y2);
        if (msgSender != null) {
          msgSender.send(new C2SDrawOval(g.getColor(), x1, y1, x2, y2));
        }
        break;
      case 5:
        this.addText(g);
        break;
      case 6:
        this.drawEraser(g, x2, y2);
        if (msgSender != null) {
          msgSender.send(new C2SDrawEraser(x2, y2));
        }
        break;
    }
  }

  private void addText(Graphics g) {
    if (textBox.getText().length() > 0) {
      this.drawText(g, textBox.getText(), textBox.getX()+1, textBox.getY()+24);
    }
    if (msgSender != null) {
      msgSender.send(new C2SDrawText(g.getColor(), textBox.getText(), textBox.getX()+1, textBox.getY()+24));
    }
    textBox.setText("");
    textBox.setLocation(x2, y2);
    textBox.setForeground(getForeground());
    this.add(textBox);
    textBox.requestFocus();    
  }

  public synchronized void drawPencil(Graphics g, int[] xPath, int[] yPath, int pathLength) {
    g.drawPolyline(xPath, yPath, pathLength);
  }

  public synchronized void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
    g.drawLine(x1, y1, x2, y2);
  }

  public synchronized void drawRect(Graphics g, int x1, int y1, int x2, int y2) {
    g.drawRect(Math.min(x1, x2), Math.min(y1, y2), 
              Math.abs(x1 - x2), Math.abs(y1 - y2));
  }

  public synchronized void drawOval(Graphics g, int x1, int y1, int x2, int y2) {
    g.drawOval(Math.min(x1, x2), Math.min(y1, y2), 
              Math.abs(x1 - x2), Math.abs(y1 - y2));
  }

  public synchronized void drawText(Graphics g, String content, int x, int y) {
    g.setFont(textBox.getFont().deriveFont(Font.PLAIN, (float)24));
    g.drawString(content, x, y);
  }

  public synchronized void drawEraser(Graphics g, int x2, int y2) {
    g.setColor(Color.white);
    g.fillOval(x2 - 20, y2 - 20, 40, 40);
  }
}
