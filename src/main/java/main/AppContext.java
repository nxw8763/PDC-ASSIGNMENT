package main;

import controller.AuditController;
import controller.CategoryController;
import controller.LoginController;
import controller.OverviewController;
import controller.TicketController;
import controller.UserController;

public class AppContext {

    private final TicketController ticketController;
    private final CategoryController categoryController;
    private final UserController userController;
    private final AuditController auditController;
    private final LoginController loginController;
    private final OverviewController overviewController;
    
    public AppContext(
            TicketController ticketController,
            CategoryController categoryController,
            UserController userController,
            AuditController auditController,
            LoginController loginController,
            OverviewController overviewController
    ) {
        this.ticketController = ticketController;
        this.categoryController = categoryController;
        this.userController  = userController;
        this.auditController = auditController;
        this.loginController = loginController;
        this.overviewController = overviewController;
    }

    public TicketController getTicketController() {
    	return ticketController;
    }
    
    public CategoryController getCategoryController() {
    	return categoryController;
    }
    
    public UserController getUserController() {
    	return userController;
    }
    
    public AuditController getAuditController() {
    	return auditController;
    }
    
    public LoginController getLoginController() {
    	return loginController;
    }
    
    public OverviewController getOverviewController() {
    	return this.overviewController;
    }
}