/*
Hansen Hong
1059012

COMP90015 s1 Assignment2
*/

import java.util.concurrent.CountDownLatch;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.BorderFactory;

public class LoginWindow extends JFrame {
  private String username = "";
  private CountDownLatch cdLatch = new CountDownLatch(1);
  
  public LoginWindow(String info) {
    setSize(500, 250);
    setTitle("Shared White Board - Login");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLayout(new GridLayout(3, 1));

    JLabel label = new JLabel(info);
    label.setFont(label.getFont().deriveFont(Font.PLAIN, (float)18));
    label.setBorder(BorderFactory.createEmptyBorder(10, 80, 0, 0));
    add(label);

    JPanel panelUsername = new JPanel(new BorderLayout());
    panelUsername.setBorder(BorderFactory.createEmptyBorder(0, 80, 30, 80));
    add(panelUsername, BorderLayout.CENTER);
    JTextField usernameBox = new JTextField();
    usernameBox.setFont(usernameBox.getFont().deriveFont(Font.PLAIN, (float)22));
    usernameBox.setEditable(true);
    panelUsername.add(usernameBox);

    JPanel panelBtn = new JPanel(new BorderLayout());
    panelBtn.setBorder(BorderFactory.createEmptyBorder(0, 200, 30, 200));
    add(panelBtn, BorderLayout.CENTER);
    JButton btnLogin = new JButton("Login");
    btnLogin.setFocusPainted(false);
    btnLogin.setFont(btnLogin.getFont().deriveFont(Font.PLAIN, (float)18));
    panelBtn.add(btnLogin);
    btnLogin.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        if (usernameBox.getText().trim().length() > 0) {
          username = usernameBox.getText().trim();
          try {
            cdLatch.countDown();
          } catch (Exception e) {}
        }
      }
    });
    setVisible(true);
  }

  public String getUsername() {
    try {
      cdLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return username;
  }
}