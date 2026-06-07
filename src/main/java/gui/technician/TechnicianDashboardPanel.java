package gui.technician;

import java.awt.Window;

import javax.swing.SwingUtilities;

import gui.abstracts.AbstractDashboardPanel;
import gui.tickets.TicketBoardPanel;
import model.tickets.Ticket;
import model.users.Technician;
import service.TicketService;

public class TechnicianDashboardPanel extends AbstractDashboardPanel {

    private final Technician technician;
    private final TicketService ticketService;

    public TechnicianDashboardPanel(
            Technician technician,
            TicketService ticketService
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
                        null,
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