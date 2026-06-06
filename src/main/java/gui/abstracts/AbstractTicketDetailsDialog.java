package gui.abstracts;

import model.Comment;
import model.Ticket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public abstract class AbstractTicketDetailsDialog extends JDialog {

    protected final Ticket ticket;

    private JLabel titleLabel;
    private JLabel categoryLabel;
    private JLabel priorityLabel;
    private JLabel statusLabel;
    private JLabel technicianLabel;
    private JLabel createdLabel;

    private JTextArea descriptionArea;

    protected JPanel commentsPanel;

    public AbstractTicketDetailsDialog(
            Window owner,
            Ticket ticket
    ) {
        super(owner);

        this.ticket = ticket;

        initialise();
    }

    private void initialise() {

        setTitle("Ticket #" + ticket.getTicketID());

        setModal(true);

        setSize(900, 700);

        setLocationRelativeTo(getOwner());

        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);

        loadTicket();
    }

    private JPanel createHeaderPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel();

        titleLabel.setFont(
                titleLabel.getFont().deriveFont(
                        Font.BOLD,
                        24f
                )
        );

        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    private JScrollPane createCenterPanel() {

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(new EmptyBorder(0, 20, 20, 20));

        categoryLabel = new JLabel();
        priorityLabel = new JLabel();
        statusLabel = new JLabel();
        technicianLabel = new JLabel();
        createdLabel = new JLabel();

        descriptionArea = new JTextArea();

        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        panel.add(categoryLabel);
        panel.add(Box.createVerticalStrut(5));

        panel.add(priorityLabel);
        panel.add(Box.createVerticalStrut(5));

        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(5));

        panel.add(technicianLabel);
        panel.add(Box.createVerticalStrut(5));

        panel.add(createdLabel);
        panel.add(Box.createVerticalStrut(15));

        panel.add(new JLabel("Description"));
        panel.add(Box.createVerticalStrut(5));

        panel.add(new JScrollPane(descriptionArea));

        panel.add(Box.createVerticalStrut(20));

        panel.add(new JLabel("Comments"));

        panel.add(Box.createVerticalStrut(10));

        commentsPanel = new JPanel();

        commentsPanel.setLayout(
                new BoxLayout(
                        commentsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.add(commentsPanel);

        return new JScrollPane(panel);
    }

    protected void loadTicket() {

        titleLabel.setText(ticket.getTitle());

        categoryLabel.setText(
                "Category: " + ticket.getCategory()
        );

        priorityLabel.setText(
                "Priority: " + ticket.getPriority()
        );

        statusLabel.setText(
                "Status: " + ticket.getStatus()
        );

        technicianLabel.setText(
                "Assigned Technician: "
                        + (ticket.getAssignedTechnicianEmail() == null
                        ? "Unassigned"
                        : String.valueOf(ticket.getAssignedTechnicianEmail()))
        );

        createdLabel.setText(
                "Created: "
                        + ticket.getCreatedDate()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy HH:mm"
                                )
                        )
        );

        descriptionArea.setText(
                ticket.getDescription()
        );

        loadComments();
    }

    protected void loadComments() {

        commentsPanel.removeAll();

        for (Comment comment : ticket.getComments()) {

            JPanel card = new JPanel(
                    new BorderLayout()
            );

            card.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    Color.LIGHT_GRAY
                            ),
                            new EmptyBorder(
                                    10,
                                    10,
                                    10,
                                    10
                            )
                    )
            );

            JLabel title = new JLabel(
                    comment.getTitle()
            );

            title.setFont(
                    title.getFont()
                            .deriveFont(Font.BOLD)
            );

            JTextArea body = new JTextArea(
                    comment.getDescription()
            );

            body.setEditable(false);
            body.setLineWrap(true);
            body.setWrapStyleWord(true);

            JLabel footer = new JLabel(
                    comment.getCreatedByUser()
                            + " | "
                            + comment.getCreatedDate()
            );

            card.add(title, BorderLayout.NORTH);
            card.add(body, BorderLayout.CENTER);
            card.add(footer, BorderLayout.SOUTH);

            commentsPanel.add(card);

            commentsPanel.add(
                    Box.createVerticalStrut(10)
            );
        }

        commentsPanel.revalidate();
        commentsPanel.repaint();
    }

    protected abstract JPanel createActionPanel();
}