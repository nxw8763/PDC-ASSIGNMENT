package gui.login;

import javax.swing.*;

import dao.UserDAO;

import service.UserManagementService;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        super("Service Desk Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        // ===== DAO LAYER =====
        UserDAO userDAO = new UserDAO();

        // ===== SERVICE LAYER =====
        UserManagementService userService =
                new UserManagementService(userDAO);

        setContentPane(
                new LoginPanel(
                        this,
                        userService
                )
        );

        setVisible(true);
    }
}