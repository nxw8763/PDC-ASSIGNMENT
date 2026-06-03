package gui.admin;

import gui.abstracts.AbstractDashboardPanel;
import model.Admin;
import service.CategoryService;
import service.TicketManagementService;
import service.UserManagementService;

public class AdminDashboardPanel extends AbstractDashboardPanel {

    private final Admin admin;

    private final UserManagementService userService;
    private final TicketManagementService ticketService;
    private final CategoryService categoryService;

    public AdminDashboardPanel(
            Admin admin,
            UserManagementService userService,
            TicketManagementService ticketService,
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
                new AdminOverviewPanel(ticketService)
        );

        registerPage(
                "users",
                new AdminUsersPanel(userService)
        );

        registerPage(
                "tickets",
                new AdminTicketsPanel(
                        admin,
                        ticketService
                )
        );

        registerPage(
                "categories",
                new AdminCategoriesPanel(categoryService)
        );

        registerPage(
                "reports",
                new AdminReportsPanel(ticketService)
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
                "Reports",
                "reports"
        );
    }
}