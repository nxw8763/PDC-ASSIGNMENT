package gui.admin;

import gui.abstracts.AbstractDashboardPanel;
import gui.admin.users.UserManagementPanel;
import gui.tickets.TicketBoardPanel;
import main.AppContext;
import model.users.Admin;

public class AdminDashboardPanel extends AbstractDashboardPanel {

    private final Admin admin;
    private final AppContext context;

    public AdminDashboardPanel(
            Admin admin,
            AppContext context
    ) {

        super(admin);

        this.admin = admin;
        this.context = context;

        buildPages();
        buildNavigation();
    }

    @Override
    protected void buildPages() {

        registerPage(
                "overview",
                new AdminOverviewPanel(admin, context.getOverviewController())
        );

        registerPage(
        	    "users",
        	    new UserManagementPanel(admin, context.getUserController())
    	);

        registerPage(
                "tickets",
                new TicketBoardPanel(
                        admin,
                        context.getTicketController()
        )
        );

        registerPage(
                "categories",
                new AdminCategoriesPanel(admin, context.getCategoryController())
        );

        registerPage(
                "audits",
                new AdminAuditLogPanel(admin, context.getAuditController())
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