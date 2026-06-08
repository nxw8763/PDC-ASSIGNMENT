package gui.login;

import javax.swing.JFrame;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        super("Service Desk Login");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(500, 350);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}