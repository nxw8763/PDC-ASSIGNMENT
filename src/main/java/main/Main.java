package main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import dao.CategoryDAO;
import dao.TicketDAO;
import dao.UserDAO;
import gui.login.LoginFrame;
import service.CategoryService;
import service.TicketService;
import service.UserService;
import database.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {

	   if (!ApplicationLock.acquire()) {

	        JOptionPane.showMessageDialog(
	                null,
	                "Application is already running."
	        );

	        System.exit(0);
	    }


	   try {

		    DatabaseInitializer.initialize();

		} catch (Exception e) {

		    JOptionPane.showMessageDialog(
		            null,
		            "Failed to initialize database:\n" + e.getMessage(),
		            "Startup Error",
		            JOptionPane.ERROR_MESSAGE
		    );

		    System.exit(1);
		}
	   
	   TicketDAO ticketDAO = new TicketDAO();
	   CategoryDAO categoryDAO = new CategoryDAO();
	   UserDAO userDAO = new UserDAO();

	   TicketService ticketService = new TicketService(ticketDAO);
	   CategoryService categoryService = new CategoryService(categoryDAO);
	   UserService userService = new UserService(userDAO);
	   
        SwingUtilities.invokeLater(() -> {

            new LoginFrame(ticketService, categoryService, userService);

        });
    }
}