package gui.abstracts;

import dto.CommentDTO;
import dto.TicketDetailsDTO;
import gui.employee.TicketDetailsView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class AbstractTicketDetailsDialog
        extends JDialog
        implements TicketDetailsView {

    protected final int ticketId;

    private JLabel titleLabel;

    private JLabel categoryLabel;

    private JLabel priorityLabel;

    private JLabel statusLabel;

    private JLabel technicianLabel;

    private JLabel createdLabel;

    private JTextArea descriptionArea;

    protected JPanel commentsPanel;
    
    protected JButton commentButton;

    public AbstractTicketDetailsDialog(
            Window owner,
            int ticketId) {

        super(owner);

        this.ticketId = ticketId;

        initialise();
    }

    private void initialise() {

        setTitle(
                "Ticket #" + ticketId
        );

        setModal(true);

        setSize(
                900,
                700
        );

        setLocationRelativeTo(
                getOwner()
        );

        setLayout(
                new BorderLayout()
        );

        add(
                createHeaderPanel(),
                BorderLayout.NORTH
        );

        add(
                createCenterPanel(),
                BorderLayout.CENTER
        );

        add(
                createActionPanel(),
                BorderLayout.SOUTH
        );
    }

    private JPanel createHeaderPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        titleLabel =
                new JLabel();

        titleLabel.setFont(
                titleLabel.getFont()
                        .deriveFont(
                                Font.BOLD,
                                24f
                        )
        );

        panel.add(
                titleLabel,
                BorderLayout.WEST
        );

        return panel;
    }

    private JScrollPane createCenterPanel() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBorder(
                new EmptyBorder(
                        0,
                        20,
                        20,
                        20
                )
        );

        categoryLabel = new JLabel();
        priorityLabel = new JLabel();
        statusLabel = new JLabel();
        technicianLabel = new JLabel();
        createdLabel = new JLabel();

        descriptionArea =
                new JTextArea();

        descriptionArea.setEditable(false);

        descriptionArea.setLineWrap(true);

        descriptionArea.setWrapStyleWord(true);

        panel.add(categoryLabel);
        panel.add(priorityLabel);
        panel.add(statusLabel);
        panel.add(technicianLabel);
        panel.add(createdLabel);

        panel.add(Box.createVerticalStrut(15));

        panel.add(
                new JLabel(
                        "Description"
                )
        );

        panel.add(
                new JScrollPane(
                        descriptionArea
                )
        );

        panel.add(Box.createVerticalStrut(20));

        panel.add(
                new JLabel(
                        "Comments"
                )
        );

        commentsPanel =
                new JPanel();

        commentsPanel.setLayout(
                new BoxLayout(
                        commentsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.add(commentsPanel);

        return new JScrollPane(panel);
    }

    @Override
    public int getTicketId() {

        return ticketId;
    }

    @Override
    public void displayTicket(
            TicketDetailsDTO dto) {

        titleLabel.setText(
                dto.getTitle()
        );

        categoryLabel.setText(
                "Category: "
                        + dto.getCategory()
        );

        priorityLabel.setText(
                "Priority: "
                        + dto.getPriority()
        );

        statusLabel.setText(
                "Status: "
                        + dto.getStatus()
        );

        technicianLabel.setText(
                "Assigned Technician: "
                        + dto.getAssignedTechnician()
        );

        createdLabel.setText(
                "Created: "
                        + dto.getCreatedDate()
        );

        descriptionArea.setText(
                dto.getDescription()
        );

        displayComments(
                dto
        );
    }

    private void displayComments(
            TicketDetailsDTO dto) {

        commentsPanel.removeAll();

        for (CommentDTO comment :
                dto.getComments()) {

            JPanel card =
                    new JPanel(
                            new BorderLayout()
                    );

            JLabel title =
                    new JLabel(
                            comment.getTitle()
                    );

            JTextArea body =
                    new JTextArea(
                            comment.getDescription()
                    );

            JLabel footer =
                    new JLabel(
                            comment.getCreatedBy()
                                    + " | "
                                    + comment.getCreatedDate()
                    );

            body.setEditable(false);

            card.add(
                    title,
                    BorderLayout.NORTH
            );

            card.add(
                    body,
                    BorderLayout.CENTER
            );

            card.add(
                    footer,
                    BorderLayout.SOUTH
            );

            commentsPanel.add(card);
        }

        commentsPanel.revalidate();
        commentsPanel.repaint();
    }

    @Override
    public void showMessage(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message
        );
    }

    @Override
    public void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
    
    @Override
    public void setAddCommentAction(
            Runnable action) {

        if (commentButton != null) {

            commentButton.addActionListener(
                    e -> action.run()
            );
        }
    }

    protected abstract JPanel createActionPanel();
}