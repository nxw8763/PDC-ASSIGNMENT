package gui.technician;

import gui.abstracts.AbstractDashboardPanel;
import gui.tickets.TicketBoardPanel;
import main.AppContext;
import model.users.Technician;

public class TechnicianDashboardPanel
        extends AbstractDashboardPanel {

    private final Technician technician;

    private final AppContext context;

    public TechnicianDashboardPanel(
            Technician technician,
            AppContext context) {

        super(
                technician
        );

        this.technician =
                technician;

        this.context = context;

        buildPages();
        buildNavigation();
    }

    @Override
    protected void buildPages() {

        registerPage(
                "KANBAN_BOARD",
                new TicketBoardPanel(
                        technician,
                        context.getTicketController()
                )
        );

        cardLayout.show(
                contentPanel,
                "KANBAN_BOARD"
        );
    }

    @Override
    protected void buildNavigation() {

        createNavButton(
                "Ticket Board",
                "KANBAN_BOARD"
        );
    }
}