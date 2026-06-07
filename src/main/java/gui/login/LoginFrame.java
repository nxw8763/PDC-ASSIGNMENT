package gui.login;

import javax.swing.*;

import dao.UserDAO;
import service.CategoryService;
import service.TicketService;
import service.UserService;

public class LoginFrame extends JFrame {

    public LoginFrame(TicketService ticketService, CategoryService categoryService, UserService userService) {

        super("Service Desk Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        setContentPane(
                new LoginPanel(
                        this,
                        ticketService,
                        categoryService,
                        userService
                )
        );

        setVisible(true);
    }
}