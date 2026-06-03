package gui.technician;

import model.Status;
import model.Technician;
import model.Ticket;
import service.TicketManagementService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TechnicianKanbanPanel extends JPanel {

    private final Technician technician;
    private final TicketManagementService ticketService;

    private KanbanColumnPanel openColumn;
    private KanbanColumnPanel assignedColumn;
    private KanbanColumnPanel progressColumn;
    private KanbanColumnPanel resolvedColumn;

    public TechnicianKanbanPanel(
            Technician technician,
            TicketManagementService ticketService
    ) {
        this.technician = technician;
        this.ticketService = ticketService;

        initialise();
    }

    private void initialise() {

        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        openColumn = new KanbanColumnPanel("OPEN");
        assignedColumn = new KanbanColumnPanel("ASSIGNED");
        progressColumn = new KanbanColumnPanel("IN PROGRESS");
        resolvedColumn = new KanbanColumnPanel("RESOLVED");

        boardPanel.add(openColumn);
        boardPanel.add(assignedColumn);
        boardPanel.add(progressColumn);
        boardPanel.add(resolvedColumn);

        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        loadTickets();
    }

    public void loadTickets() {

        openColumn.clearTickets();
        assignedColumn.clearTickets();
        progressColumn.clearTickets();
        resolvedColumn.clearTickets();

        List<Ticket> tickets = ticketService.getAllTickets();

        for (Ticket ticket : tickets) {

            String assignedLabel =
                    (ticket.getAssignedTechnicianID() == 0)
                            ? "Unassigned"
                            : "Technician ID: " + ticket.getAssignedTechnicianID();

            TechnicianTicketCard card =
                    new TechnicianTicketCard(ticket, assignedLabel);

            card.addCardClickListener(() -> {

                Window window =
                        SwingUtilities.getWindowAncestor(this);

                TechnicianTicketDetailsDialog dialog =
                        new TechnicianTicketDetailsDialog(
                                window,
                                ticket,
                                technician,
                                ticketService
                        );

                dialog.setVisible(true);

                loadTickets();
            });

            switch (ticket.getStatus()) {

                case OPEN -> openColumn.addTicket(card);
                case ASSIGNED -> assignedColumn.addTicket(card);
                case IN_PROGRESS -> progressColumn.addTicket(card);
                case RESOLVED -> resolvedColumn.addTicket(card);
            }
        }

        revalidate();
        repaint();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) loadTickets();
    }
}