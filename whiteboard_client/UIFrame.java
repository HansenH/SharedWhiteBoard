/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import protocols.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.WindowConstants;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class UIFrame {
  public static final Color[] COLORLIST = new Color[] {
    new Color(0, 0, 0),
    new Color(255, 255, 255),
    new Color(127, 127, 127),
    new Color(195, 195, 195),
    new Color(136, 0, 21),
    new Color(185, 122, 87),
    new Color(237, 28, 36),
    new Color(255, 174, 201),
    new Color(255, 127, 39),
    new Color(255, 201, 14),
    new Color(255, 242, 0),
    new Color(239, 228, 176),
    new Color(34, 177, 76),
    new Color(181, 230, 29),
    new Color(0, 162, 232),
    new Color(153, 217, 234),
    new Color(63, 72, 204),
    new Color(112, 146, 190),
    new Color(163, 73, 164),
    new Color(200, 191, 231),
  };  // preset palette colors
  private JFrame frame;
  private JTextArea onlineUsers;
  private JTextArea chatArea;
  private Canvas canvas;
  private MsgSender msgSender;

  public UIFrame() {
    frame = new JFrame("Shared White Board");
    frame.setSize(1600, 900);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    // Painting Canvas
    canvas = new Canvas();
    canvas.setBackground(Color.white);
    frame.add(canvas, BorderLayout.CENTER);
    canvas.addMouseListener(new ClickListener());
    canvas.addMouseMotionListener(new DragListener());

    // Top Bar
    JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    frame.add(panelTop, BorderLayout.NORTH);

    JButton btnClear = new JButton("Clear");
    btnClear.setFocusPainted(false);
    panelTop.add(btnClear);
    btnClear.setFont(btnClear.getFont().deriveFont(Font.PLAIN, (float)18));
    btnClear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        clearCanvas();
        if (msgSender != null) {
          msgSender.send(new C2SClearCanvas());
        }
      }
    });

    JButton btnSave = new JButton(" Save ");
    btnSave.setFocusPainted(false);
    btnSave.setForeground(Color.gray);
    panelTop.add(btnSave);
    btnSave.setFont(btnSave.getFont().deriveFont(Font.PLAIN, (float)18));

    JButton btnLoad = new JButton(" Load ");
    btnLoad.setFocusPainted(false);
    btnLoad.setForeground(Color.gray);
    panelTop.add(btnLoad);
    btnLoad.setFont(btnLoad.getFont().deriveFont(Font.PLAIN, (float)18));

    JButton btnAbout = new JButton("About");
    btnAbout.setFocusPainted(false);
    panelTop.add(btnAbout);
    btnAbout.setFont(btnAbout.getFont().deriveFont(Font.PLAIN, (float)18));
    btnAbout.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        JOptionPane.showMessageDialog(frame, 
          "Assignment2 of COMP90015 2021S1\n by Hansen Hong", 
          "About", JOptionPane.INFORMATION_MESSAGE);
      }
    });

    // Left Sidebar
    JPanel panelLeft = new JPanel(new BorderLayout());
    panelLeft.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
    frame.add(panelLeft, BorderLayout.WEST);
    
    // Tool Selecting Panel
    JPanel panelTools = new JPanel(new GridLayout(6, 1, 0, 6));
    panelTools.setBorder(BorderFactory.createTitledBorder(
                          BorderFactory.createEtchedBorder(), "Tools"));
    panelLeft.add(panelTools, BorderLayout.NORTH);
    JRadioButton btnPencil = new JRadioButton("Pencil");
    panelTools.add(btnPencil);
    btnPencil.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        canvas.setToolType(1);
      }
    });
    JRadioButton btnLine = new JRadioButton("Line");
    panelTools.add(btnLine);
    btnLine.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        canvas.setToolType(2);
      }
    });
    JRadioButton btnRectangle = new JRadioButton("Rect");
    panelTools.add(btnRectangle);
    btnRectangle.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        canvas.setToolType(3);
      }
    });
    JRadioButton btnOval = new JRadioButton("Oval");
    panelTools.add(btnOval);
    btnOval.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        canvas.setToolType(4);
      }
    });
    JRadioButton btnText = new JRadioButton("Text");
    panelTools.add(btnText);
    btnText.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        canvas.setToolType(5);
      }
    });
    JRadioButton btnEraser = new JRadioButton("Eraser");
    panelTools.add(btnEraser);
    btnEraser.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        canvas.setToolType(6);
      }
    });
    ButtonGroup tools = new ButtonGroup();
    tools.add(btnPencil);
    tools.add(btnLine);
    tools.add(btnRectangle);
    tools.add(btnOval);
    tools.add(btnText);
    tools.add(btnEraser);
    btnPencil.setSelected(true);
    this.canvas.setToolType(1);

    // Color Selecting Panel
    JPanel panelColors = new JPanel(new GridLayout(UIFrame.COLORLIST.length / 2, 2, 2, 2));
    panelColors.setBorder(BorderFactory.createTitledBorder(
                          BorderFactory.createEtchedBorder(), "Colors"));
    panelLeft.add(panelColors, BorderLayout.SOUTH);
    JRadioButton[] colorButtons = new JRadioButton[UIFrame.COLORLIST.length];
    ButtonGroup colors = new ButtonGroup();
    for (int i = 0; i < UIFrame.COLORLIST.length; ++i) {
      colorButtons[i] = new JRadioButton(" ");
      colorButtons[i].setFont(colorButtons[i].getFont().deriveFont(Font.BOLD, (float)20));
      colorButtons[i].setBackground(UIFrame.COLORLIST[i]);
      colorButtons[i].setFocusPainted(false);
      panelColors.add(colorButtons[i]);
      colors.add(colorButtons[i]);
      colorButtons[i].addActionListener(new btnColorActionListener(i));
    }
    colorButtons[0].setSelected(true);
    this.canvas.setColor(UIFrame.COLORLIST[0]);

    // Right Sidebar
    JPanel panelRight = new JPanel(new BorderLayout());
    panelRight.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
    frame.add(panelRight, BorderLayout.EAST);

    // Online Participants
    JPanel panelParticipants = new JPanel(new BorderLayout());
    panelParticipants.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "Online Participants"));
    panelRight.add(panelParticipants, BorderLayout.NORTH);
    
    onlineUsers = new JTextArea(14, 13);
    onlineUsers.setBackground(new Color(250, 250, 250));
    onlineUsers.setFont(onlineUsers.getFont().deriveFont(Font.BOLD, (float)20));
    onlineUsers.setEditable(false);
    onlineUsers.setLineWrap(false);
    JScrollPane onlineUsersScrollPane = new JScrollPane(onlineUsers);
    panelParticipants.add(onlineUsersScrollPane, BorderLayout.CENTER);

    // Chat Panel
    JPanel panelChat = new JPanel(new BorderLayout());
    panelChat.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(), "Room Chat"));
    panelRight.add(panelChat, BorderLayout.CENTER);

    chatArea = new JTextArea();
    chatArea.setBackground(new Color(250, 250, 250));
    chatArea.setFont(chatArea.getFont().deriveFont(Font.PLAIN, (float)16));
    chatArea.setEditable(false);
    chatArea.setLineWrap(true);
    JScrollPane chatAreaScrollPane = new JScrollPane(chatArea);
    panelChat.add(chatAreaScrollPane, BorderLayout.CENTER);

    JPanel panelChatText = new JPanel(new BorderLayout());
    panelChatText.setBorder(BorderFactory.createEmptyBorder(8, 2, 8, 2));
    panelChat.add(panelChatText, BorderLayout.SOUTH);

    JTextField chatTextField = new JTextField();
    chatTextField.setFont(chatTextField.getFont().deriveFont(Font.PLAIN, (float)18));
    chatTextField.setEditable(true);
    panelChatText.add(chatTextField, BorderLayout.CENTER);

    JButton btnChat = new JButton("Send");
    panelChatText.add(btnChat, BorderLayout.EAST);
    btnChat.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        if (msgSender != null && chatTextField.getText().length() > 0) {
          msgSender.send(new C2SChat(chatTextField.getText()));
          chatTextField.setText("");
        }
      }
    });
    
    frame.setVisible(true);
  }

  class btnColorActionListener implements ActionListener {
    private int i;
    public btnColorActionListener(int i) {
      this.i = i;
    }
    @Override
    public void actionPerformed(ActionEvent event) {
      canvas.setColor(UIFrame.COLORLIST[i]);
    }
  }

  class ClickListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent event) {
      canvas.setIdle(false);
      canvas.setPoint1(event.getX(), event.getY());
      canvas.setPoint2(event.getX(), event.getY());
    }

    @Override
    public void mouseReleased(MouseEvent event) {
      canvas.draw();
      canvas.setIdle(true);
      canvas.repaint();
    }
  }

  class DragListener extends MouseMotionAdapter {
    @Override
    public void mouseDragged(MouseEvent event){
      canvas.setPoint2(event.getX(), event.getY());
      canvas.repaint();
    }
  }

  public void closeWindow() {
    frame.dispose();
  }

  public void exit() {
    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
  }

  public void setMsgSender(MsgSender msgSender) {
    this.msgSender = msgSender;
    canvas.setMsgSender(msgSender);
  }

  public boolean lostConnection() {
    //show message
    int ans = JOptionPane.showConfirmDialog(frame, 
        "Please Chech you network.\nDo you want to reconnect?",
        "Connection lost!", JOptionPane.YES_NO_OPTION);
    if (ans == JOptionPane.YES_OPTION) {
      return true;  // select to reconnect
    } else {
      return false;
    }
  }

  public void updateChat(String text) {
    chatArea.append(text);
  }

  public void updateUserList(String userList) {
    if (userList == null) {
      onlineUsers.setText("-- Offline Mode --");
    } else {
      onlineUsers.setText(userList);
    }
  }

  public void clearCanvas() {
    canvas.clearAll();
    canvas.repaint();
  }

  public void initCanvas(BufferedImage image) {
    canvas.setImage(image);
    canvas.repaint();
  }

  public void drawPencil(Color c, int[] xPath, int[] yPath, int pathLength) {
    Graphics g = canvas.getImage().getGraphics();
    g.setColor(c);
    canvas.drawPencil(g, xPath, yPath, pathLength);
    canvas.repaint();
  }

  public void drawLine(Color c, int x1, int y1, int x2, int y2) {
    Graphics g = canvas.getImage().getGraphics();
    g.setColor(c);
    canvas.drawLine(g, x1, y1, x2, y2);
    canvas.repaint();
  }

  public void drawRect(Color c, int x1, int y1, int x2, int y2) {
    Graphics g = canvas.getImage().getGraphics();
    g.setColor(c);
    canvas.drawRect(g, x1, y1, x2, y2);
    canvas.repaint();
  }

  public void drawOval(Color c, int x1, int y1, int x2, int y2) {
    Graphics g = canvas.getImage().getGraphics();
    g.setColor(c);
    canvas.drawOval(g, x1, y1, x2, y2);
    canvas.repaint();
  }

  public void drawText(Color c, String content, int x, int y) {
    Graphics g = canvas.getImage().getGraphics();
    g.setColor(c);
    canvas.drawText(g, content, x, y);
    canvas.repaint();
  }

  public void drawEraser(int x, int y) {
    Graphics g = canvas.getImage().getGraphics();
    canvas.drawEraser(g, x, y);
    canvas.repaint();
  }
}