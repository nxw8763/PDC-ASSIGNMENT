package gui.employee;

import gui.abstracts.AbstractDashboardPanel;
import model.users.Employee;
import service.CategoryService;
import service.TicketService;

public class EmployeeDashboardPanel extends AbstractDashboardPanel {

    private final Employee employee;
    private final TicketService ticketService;
    private final CategoryService categoryService;

    public static final String MY_TICKETS = "MY_TICKETS";
    public static final String CREATE_TICKET = "CREATE_TICKET";

    public EmployeeDashboardPanel(
            Employee employee,
            TicketService ticketService,
            CategoryService categoryService
    ) {
        super(employee);

        this.employee = employee;
        this.ticketService = ticketService;
        this.categoryService = categoryService;

        buildPages();
        buildNavigation();
    }

    @Override
    protected void buildPages() {

        registerPage(
                MY_TICKETS,
                new MyTicketsPanel(employee, ticketService)
        );

        registerPage(
                CREATE_TICKET,
                new CreateTicketPanel(employee, ticketService, categoryService)
        );
    }

    @Override
    protected void buildNavigation() {

        createNavButton("My Tickets", MY_TICKETS);
        createNavButton("Create Ticket", CREATE_TICKET);
    }
}