package gui.login;

import controller.LoginController;
import controller.NavigationController;
import model.users.User;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final NavigationController navigationController;
    private final LoginController loginController;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(
            NavigationController navigationController,
            LoginController loginController
    ) {

        this.navigationController =
                navigationController;

        this.loginController =
                loginController;

        buildUI();
    }

    private void buildUI() {

        setLayout(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        10,
                        10,
                        10,
                        10
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        JLabel title =
                new JLabel(
                        "Service Desk Login"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        usernameField =
                new JTextField(20);

        passwordField =
                new JPasswordField(20);

        JButton loginButton =
                new JButton("Login");

        loginButton.addActionListener(
                e -> login()
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;

        add(
                new JLabel("Username:"),
                gbc
        );

        gbc.gridx = 1;

        add(
                usernameField,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy++;

        add(
                new JLabel("Password:"),
                gbc
        );

        gbc.gridx = 1;

        add(
                passwordField,
                gbc
        );

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        add(
                loginButton,
                gbc
        );
    }

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );
        User user;
        try {
        	user =
                loginController.login(
                        username,
                        password
                );

	    } catch (SecurityException e) {
	            JOptionPane.showMessageDialog(
	                    this,
	                    "Invalid username/password.",
	                    "Login Failed",
	                    JOptionPane.ERROR_MESSAGE
	            );
	
	            return;
	    }
        
        navigationController.showDashboard(
                user
        );
    }
}