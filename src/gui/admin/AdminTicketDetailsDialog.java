package gui.admin;

import gui.abstracts.AbstractTicketDetailsDialog;

import model.Admin;
import model.Priority;
import model.Status;
import model.Ticket;

import service.TicketManagementService;

import javax.swing.*;
import java.awt.*;

public class AdminTicketDetailsDialog
        extends AbstractTicketDetailsDialog {

    private final Admin admin;
    private final TicketManagementService ticketService;

    public AdminTicketDetailsDialog(
            Window owner,
            Ticket ticket,
            Admin admin,
            TicketManagementService ticketService
    ) {

        super(owner, ticket);

        this.admin = admin;
        this.ticketService = ticketService;
    }

    @Override
    protected JPanel createActionPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JButton assignButton =
                new JButton("Assign");

        JButton unassignButton =
                new JButton("Unassign");

        JButton statusButton =
                new JButton("Change Status");

        JButton priorityButton =
                new JButton("Change Priority");

        JButton commentButton =
                new JButton("Add Comment");

        JButton closeButton =
                new JButton("Close Ticket");

        JButton reopenButton =
                new JButton("Reopen");

        panel.add(assignButton);
        panel.add(unassignButton);
        panel.add(statusButton);
        panel.add(priorityButton);
        panel.add(commentButton);
        panel.add(closeButton);
        panel.add(reopenButton);

        assignButton.addActionListener(
                e -> assignTicket()
        );

        unassignButton.addActionListener(
                e -> unassignTicket()
        );

        statusButton.addActionListener(
                e -> changeStatus()
        );

        priorityButton.addActionListener(
                e -> changePriority()
        );

        commentButton.addActionListener(
                e -> addComment()
        );

        closeButton.addActionListener(
                e -> closeTicket()
        );

        reopenButton.addActionListener(
                e -> reopenTicket()
        );

        return panel;
    }

    private void assignTicket() {

        String technicianIdString =
                JOptionPane.showInputDialog(
                        this,
                        "Technician ID"
                );

        if (technicianIdString == null
                || technicianIdString.isBlank()) {
            return;
        }

        try {

            int technicianId =
                    Integer.parseInt(
                            technicianIdString
                    );

            ticketService.assignTicket(
                    ticket.getTicketID(),
                    technicianId,
                    null,
                    admin
            );

            loadTicket();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Technician ID"
            );
        }
    }

    private void unassignTicket() {

        ticketService.unassignTicket(
                ticket.getTicketID(),
                admin
        );

        loadTicket();
    }

    private void changeStatus() {

        Status status =
                (Status) JOptionPane.showInputDialog(
                        this,
                        "Select Status",
                        "Status",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        Status.values(),
                        ticket.getStatus()
                );

        if (status == null) {
            return;
        }

        ticketService.updateStatus(
                ticket.getTicketID(),
                status,
                admin
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

        if (priority == null) {
            return;
        }

        ticketService.updatePriority(
                ticket.getTicketID(),
                priority,
                admin
        );

        loadTicket();
    }

    private void addComment() {

        String title =
                JOptionPane.showInputDialog(
                        this,
                        "Comment Title"
                );

        if (title == null
                || title.isBlank()) {
            return;
        }

        String description =
                JOptionPane.showInputDialog(
                        this,
                        "Comment Description"
                );

        if (description == null
                || description.isBlank()) {
            return;
        }

        ticketService.addComment(
                ticket.getTicketID(),
                title,
                description,
                admin.getEmail()
        );

        loadComments();
    }

    private void closeTicket() {

        ticketService.closeTicket(
                ticket.getTicketID(),
                admin
        );

        loadTicket();
    }

    private void reopenTicket() {

        ticketService.updateStatus(
                ticket.getTicketID(),
                Status.OPEN,
                admin
        );

        loadTicket();
    }
}