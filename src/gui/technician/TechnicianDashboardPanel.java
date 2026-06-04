package gui.technician;

import java.awt.Window;

import javax.swing.SwingUtilities;

import gui.abstracts.AbstractDashboardPanel;
import gui.tickets.TicketBoardPanel;
import model.Technician;
import model.Ticket;
import service.TicketManagementService;

public class TechnicianDashboardPanel extends AbstractDashboardPanel {

    private final Technician technician;
    private final TicketManagementService ticketService;

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
       registerPage(
                "KANBAN_BOARD",
                new TicketBoardPanel(
                        technician,
                        ticketService
                )
        );

        cardLayout.show(
                contentPanel,
                "KANBAN_BOARD"
        );
    }

    @Override
    protected void buildNavigation() {
        createNavButton("Ticket Board", "KANBAN_BOARD");
    }
}