package gui.technician;

import model.*;
import service.TicketManagementService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

import gui.abstracts.AbstractTicketDetailsDialog;

public class TechnicianTicketDetailsDialog extends AbstractTicketDetailsDialog {

    private final Technician technician;
    private final TicketManagementService ticketService;

    public TechnicianTicketDetailsDialog(
            Window owner,
            Ticket ticket,
            Technician technician,
            TicketManagementService ticketService
    ) {
        super(owner, ticket);

        this.technician = technician;
        this.ticketService = ticketService;
    }

    @Override
    protected JPanel createActionPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton assignButton = new JButton("Assign To Me");
        JButton unassignButton = new JButton("Unassign");
        JButton statusButton = new JButton("Change Status");
        JButton priorityButton = new JButton("Change Priority");
        JButton commentButton = new JButton("Add Comment");

        panel.add(assignButton);
        panel.add(unassignButton);
        panel.add(statusButton);
        panel.add(priorityButton);
        panel.add(commentButton);

        assignButton.addActionListener(e -> assignTicket());
        unassignButton.addActionListener(e -> unassignTicket());
        statusButton.addActionListener(e -> changeStatus());
        priorityButton.addActionListener(e -> changePriority());
        commentButton.addActionListener(e -> addComment());

        return panel;
    }

    private void assignTicket() {

        ticketService.assignTicket(
                ticket.getTicketID(),
                technician.getUserID(),
                technician.getEmail(),
                technician
        );

        loadTicket();
    }

    private void unassignTicket() {

        ticketService.unassignTicket(
                ticket.getTicketID(),
                technician
        );

        loadTicket();
    }

    private void changeStatus() {

        Status[] allowed = {
                Status.ASSIGNED,
                Status.IN_PROGRESS,
                Status.RESOLVED
        };

        Status status =
                (Status) JOptionPane.showInputDialog(
                        this,
                        "Select Status",
                        "Status",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        allowed,
                        ticket.getStatus()
                );

        if (status == null) return;

        ticketService.updateStatus(
                ticket.getTicketID(),
                status,
                technician
        );

        loadTicket();
    }

    private void changePriority() {

        Priority priority =
                (Priority) JOptionPane.showInputDialog(
                        this,
                        "Select Priority",
                        "Priority",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        Priority.values(),
                        ticket.getPriority()
                );

        if (priority == null) return;

        ticketService.updatePriority(
                ticket.getTicketID(),
                priority,
                technician
        );

        loadTicket();
    }

    private void addComment() {

        String title =
                JOptionPane.showInputDialog(this, "Comment Title");

        if (title == null || title.isBlank()) return;

        String description =
                JOptionPane.showInputDialog(this, "Comment Description");

        if (description == null || description.isBlank()) return;

        ticketService.addComment(
                ticket.getTicketID(),
                title,
                description,
                technician.getEmail()
        );

        loadComments();
    }
}