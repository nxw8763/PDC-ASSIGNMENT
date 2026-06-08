package main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller.AuditController;
import controller.CategoryController;
import controller.LoginController;
import controller.NavigationController;
import controller.OverviewController;
import controller.TicketController;
import controller.UserController;
import dao.CategoryDAO;
import dao.OverviewDAO;
import dao.TicketDAO;
import dao.UserDAO;
import gui.login.LoginFrame;
import service.CategoryService;
import service.OverviewService;
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
	   OverviewDAO overviewDAO = new OverviewDAO();

	   TicketService ticketService = new TicketService(ticketDAO);
	   CategoryService categoryService = new CategoryService(categoryDAO);
	   UserService userService = new UserService(userDAO);
	   OverviewService overviewService = new OverviewService(overviewDAO);
	   
	   TicketController ticketController =
		        new TicketController(
		                ticketService,
		                userService
		        );

		CategoryController categoryController =
		        new CategoryController(
		                categoryService
		        );

		UserController userController =
		        new UserController(
		                userService
		        );

		AuditController auditController =
		        new AuditController();

		LoginController loginController =
		        new LoginController(
		                userService
		        );
		
		OverviewController overviewController =
				new OverviewController(
						overviewService
				);

		AppContext context =
		        new AppContext(
		                ticketController,
		                categoryController,
		                userController,
		                auditController,
		                loginController,
		                overviewController
		        );

		LoginFrame loginFrame =
		        new LoginFrame();

		NavigationController navigationController =
		        new NavigationController(
		                loginFrame,
		                context
		        );

		SwingUtilities.invokeLater(
		        navigationController::showLogin
		);
    }
} 