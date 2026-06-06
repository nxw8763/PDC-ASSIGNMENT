package gui.admin;

import gui.abstracts.AbstractTicketDetailsDialog;

import model.Admin;
import model.Technician;
import model.Ticket;
import model.enums.Priority;
import model.enums.Status;
import service.TicketManagementService;
import service.UserManagementService;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class AdminTicketDetailsDialog
        extends AbstractTicketDetailsDialog {

    private final Admin admin;
    private final UserManagementService userService;
    private final TicketManagementService ticketService;

    public AdminTicketDetailsDialog(
            Window owner,
            Ticket ticket,
            Admin admin,
            UserManagementService userService,
            TicketManagementService ticketService
    ) {
        super(owner, ticket);

        this.admin = admin;
        this.ticketService = ticketService;
        this.userService = userService;
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

        List<Technician> technicians =
                userService.getAllTechnicians(admin);

        if (technicians.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No technicians available."
            );

            return;
        }

        JComboBox<Technician> technicianCombo =
                new JComboBox<>(
                        technicians.toArray(
                                new Technician[0]
                        )
                );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        technicianCombo,
                        "Select Technician",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        Technician technician =
                (Technician) technicianCombo
                        .getSelectedItem();

        if (technician == null) {
            return;
        }

        ticketService.assignTicket(
                ticket.getTicketID(),
                technician.getUserID(),
                technician.getEmail(),
                admin
        );

        loadTicket();
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
                admin
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