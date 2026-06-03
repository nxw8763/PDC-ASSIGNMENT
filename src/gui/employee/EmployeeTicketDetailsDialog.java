package gui.employee;

import model.Comment;
import model.Employee;
import model.Ticket;
import service.TicketManagementService;

import javax.swing.*;

import gui.abstracts.AbstractTicketDetailsDialog;

import java.awt.*;
import java.time.LocalDateTime;

public class EmployeeTicketDetailsDialog
        extends AbstractTicketDetailsDialog {

    private final Employee employee;
    private final TicketManagementService ticketService;

    public EmployeeTicketDetailsDialog(
            Window owner,
            Ticket ticket,
            Employee employee,
            TicketManagementService ticketService
    ) {

        super(owner, ticket);

        this.employee = employee;
        this.ticketService = ticketService;
    }

    @Override
    protected JPanel createActionPanel() {

        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT
                )
        );

        JButton commentButton =
                new JButton("Add Comment");

        commentButton.addActionListener(
                e -> addComment()
        );

        panel.add(commentButton);

        return panel;
    }

    private void addComment() {

        String title = JOptionPane.showInputDialog(this, "Comment Title");
        if (title == null || title.isBlank()) return;

        String description = JOptionPane.showInputDialog(this, "Comment Description");
        if (description == null || description.isBlank()) return;

        ticketService.addComment(
                ticket.getTicketID(),
                title,
                description,
                employee.getEmail()
        );

        loadComments();
    }
}