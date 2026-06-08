package gui.employee;

import gui.abstracts.AbstractDashboardPanel;
import main.AppContext;
import model.users.Employee;

public class EmployeeDashboardPanel extends AbstractDashboardPanel {

    private final Employee employee;
    private final AppContext context;

    public static final String MY_TICKETS = "MY_TICKETS";
    public static final String CREATE_TICKET = "CREATE_TICKET";

    public EmployeeDashboardPanel(
            Employee employee,
            AppContext context
    ) {
        super(employee);

        this.employee = employee;
        this.context = context;

        buildPages();
        buildNavigation();
    }

    @Override
    protected void buildPages() {

        registerPage(
                MY_TICKETS,
                new MyTicketsPanel(employee, context.getTicketController())
        );

        registerPage(
                CREATE_TICKET,
                new CreateTicketPanel(employee, context.getTicketController(), context.getCategoryController())
        );
    }

    @Override
    protected void buildNavigation() {

        createNavButton("My Tickets", MY_TICKETS);
        createNavButton("Create Ticket", CREATE_TICKET);
    }
}