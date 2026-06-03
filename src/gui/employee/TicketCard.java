package gui.employee;

import model.Ticket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TicketCard extends JPanel {

    private final Ticket ticket;

    public TicketCard(Ticket ticket, Runnable onView) {

        this.ticket = ticket;

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220,220,220)
                        ),
                        new EmptyBorder(15,15,15,15)
                )
        );

        setPreferredSize(new Dimension(280, 180));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titleLabel =
                new JLabel(ticket.getTitle());

        titleLabel.setFont(
                titleLabel.getFont()
                        .deriveFont(Font.BOLD, 16f)
        );

        JLabel idLabel =
                new JLabel("Ticket #" + ticket.getTicketID());

        JLabel categoryLabel =
                new JLabel("Category: " + ticket.getCategory());

        JLabel priorityLabel =
                new JLabel("Priority: " + ticket.getPriority());

        JLabel statusLabel =
                new JLabel("Status: " + ticket.getStatus());

        JLabel dateLabel =
                new JLabel(
                        "Created: "
                                + ticket.getCreatedDate()
                                        .toLocalDate()
                );

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(idLabel);
        content.add(categoryLabel);
        content.add(priorityLabel);
        content.add(statusLabel);
        content.add(dateLabel);

        JButton viewButton =
                new JButton("View Details");

        viewButton.addActionListener(e -> onView.run());

        add(content, BorderLayout.CENTER);
        add(viewButton, BorderLayout.SOUTH);
    }

    public Ticket getTicket() {
        return ticket;
    }
}