package gui.technician;

import gui.abstracts.AbstractDashboardPanel;
import model.Technician;
import service.TicketManagementService;

public class TechnicianDashboardPanel extends AbstractDashboardPanel {

    private final Technician technician;
    private final TicketManagementService ticketService;

    private TechnicianKanbanPanel kanbanPanel;

    public TechnicianDashboardPanel(
            Technician technician,
            TicketManagementService ticketService
    ) {
        super(technician);

        this.technician = technician;
        this.ticketService = ticketService;

        buildPages();
        buildNavigation();
    }

    @Override
    protected void buildPages() {

        kanbanPanel =
                new TechnicianKanbanPanel(
                        technician,
                        ticketService
                );

        registerPage("KANBAN_BOARD", kanbanPanel);

        cardLayout.show(contentPanel, "KANBAN_BOARD");
    }

    @Override
    protected void buildNavigation() {
        createNavButton("Ticket Board", "KANBAN_BOARD");
    }
}