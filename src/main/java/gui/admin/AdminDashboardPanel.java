package gui.admin;

import java.awt.Window;

import javax.swing.SwingUtilities;

import gui.abstracts.AbstractDashboardPanel;
import gui.admin.users.UserManagementPanel;
import gui.tickets.TicketBoardPanel;
import model.tickets.Ticket;
import model.users.Admin;
import service.CategoryService;
import service.TicketService;
import service.UserService;

public class AdminDashboardPanel extends AbstractDashboardPanel {

    private final Admin admin;

    private final UserService userService;
    private final TicketService ticketService;
    private final CategoryService categoryService;

    public AdminDashboardPanel(
            Admin admin,
            UserService userService,
            TicketService ticketService,
            CategoryService categoryService
    ) {

        super(admin);

        this.admin = admin;

        this.userService = userService;
        this.ticketService = ticketService;
        this.categoryService = categoryService;

        buildPages();
        buildNavigation();
    }

    @Override
    protected void buildPages() {

        registerPage(
                "overview",
                new AdminOverviewPanel(admin)
        );

        registerPage(
        	    "users",
        	    new UserManagementPanel(admin, userService)
    	);

        registerPage(
                "tickets",
                new TicketBoardPanel(
                        admin,
                        userService,
                        ticketService
        )
        );

        registerPage(
                "categories",
                new AdminCategoriesPanel(admin, categoryService)
        );

        registerPage(
                "audits",
                new AdminAuditLogPanel(admin)
        );

        cardLayout.show(contentPanel, "overview");
    }

    @Override
    protected void buildNavigation() {

        createNavButton(
                "Overview",
                "overview"
        );

        createNavButton(
                "Users",
                "users"
        );

        createNavButton(
                "Tickets",
                "tickets"
        );

        createNavButton(
                "Categories",
                "categories"
        );

        createNavButton(
                "Audits",
                "audits"
        );
    }
}