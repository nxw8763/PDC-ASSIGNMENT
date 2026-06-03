package gui.employee;

import model.Employee;
import service.CategoryService;
import service.TicketManagementService;
import database.UserDatabase;
import gui.abstracts.AbstractDashboardPanel;

public class EmployeeDashboardPanel extends AbstractDashboardPanel {

    private final Employee employee;
    private final TicketManagementService ticketService;
    private final CategoryService categoryService;

    public static final String MY_TICKETS = "MY_TICKETS";
    public static final String CREATE_TICKET = "CREATE_TICKET";

    public EmployeeDashboardPanel(Employee employee,
            UserDatabase userDatabase) {

	super(employee);
	
	this.employee = employee;
	this.ticketService =
	new TicketManagementService(userDatabase);
	this.categoryService =
	new CategoryService();
	
	buildPages();
	buildNavigation();
}

    @Override
    protected void buildPages() {

        registerPage(
                MY_TICKETS,
                new MyTicketsPanel(this.employee, this.ticketService)
        );

        registerPage(
                CREATE_TICKET,
                new CreateTicketPanel(
                        this.employee,
                        this.ticketService,
                        this.categoryService
                )
        );
    }

    @Override
    protected void buildNavigation() {

        createNavButton("My Tickets", MY_TICKETS);

        createNavButton("Create Ticket", CREATE_TICKET);
    }
}