package gui.tickets;

import gui.admin.AdminTicketDetailsDialog;
import gui.employee.EmployeeTicketDetailsDialog;
import gui.technician.TechnicianTicketDetailsDialog;
import model.enums.Status;
import model.tickets.Ticket;
import model.users.Admin;
import model.users.Employee;
import model.users.Technician;
import model.users.User;
import service.TicketService;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TicketBoardPanel extends JPanel {

    private final User currentUser;
    private final TicketService ticketService;
    private final UserService userService;
    
    private final KanbanColumnPanel openColumn;
    private final KanbanColumnPanel assignedColumn;
    private final KanbanColumnPanel progressColumn;
    private final KanbanColumnPanel resolvedColumn;

    public TicketBoardPanel(
            User currentUser,
            UserService userService,
            TicketService ticketService
    ) {

        this.currentUser = currentUser;
        this.userService = userService;
        this.ticketService = ticketService;

        setLayout(new BorderLayout());

        JPanel boardPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                0
                        )
                );

        boardPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        openColumn =
                new KanbanColumnPanel("OPEN");

        assignedColumn =
                new KanbanColumnPanel("ASSIGNED");

        progressColumn =
                new KanbanColumnPanel("IN PROGRESS");

        resolvedColumn =
                new KanbanColumnPanel("RESOLVED");

        boardPanel.add(openColumn);
        boardPanel.add(assignedColumn);
        boardPanel.add(progressColumn);
        boardPanel.add(resolvedColumn);

        JScrollPane scrollPane =
                new JScrollPane(boardPanel);

        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        refreshBoard();
    }

    public void refreshBoard() {

        List<Ticket> tickets =
                ticketService.getVisibleTickets(currentUser);

        setTickets(tickets);
    }

    private void setTickets(List<Ticket> tickets) {

        openColumn.clearCards();
        assignedColumn.clearCards();
        progressColumn.clearCards();
        resolvedColumn.clearCards();

        for (Ticket ticket : tickets) {

            TicketCard card =
                    new TicketCard(
                            ticket,
                            () -> openTicket(ticket)
                    );

            switch (ticket.getStatus()) {

                case OPEN ->
                        openColumn.addCard(card);

                case ASSIGNED ->
                        assignedColumn.addCard(card);

                case IN_PROGRESS ->
                        progressColumn.addCard(card);

                case RESOLVED ->
                        resolvedColumn.addCard(card);

                default -> {
                }
            }
        }

        revalidate();
        repaint();
    }

    private void openTicket(Ticket ticket) {

        Window owner =
                SwingUtilities.getWindowAncestor(this);

        JDialog dialog = createDialog(owner, ticket);

        if (dialog == null) {
            return;
        }

        dialog.setVisible(true);

        refreshBoard();
    }

    private JDialog createDialog(
            Window owner,
            Ticket ticket
    ) {

        if (currentUser instanceof Employee employee) {

            return new EmployeeTicketDetailsDialog(
                    owner,
                    ticket,
                    employee,
                    ticketService
            );
        }

        if (currentUser instanceof Technician technician) {

            return new TechnicianTicketDetailsDialog(
                    owner,
                    ticket,
                    technician,
                    ticketService
            );
        }

        if (currentUser instanceof Admin admin) {

            return new AdminTicketDetailsDialog(
                    owner,
                    ticket,
                    admin,
                    userService,
                    ticketService
            );
        }

        return null;
    }
}