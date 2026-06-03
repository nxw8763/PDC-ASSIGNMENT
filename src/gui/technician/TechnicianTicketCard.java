package gui.technician;

import model.Ticket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

public class TechnicianTicketCard extends JPanel {

    private final Ticket ticket;

    public TechnicianTicketCard(
            Ticket ticket
    ) {

        this.ticket = ticket;

        initialise();
    }

    private void initialise() {

        setLayout(
                new BoxLayout(
                        this,
                        BoxLayout.Y_AXIS
                )
        );

        setBorder(
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

        setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        130
                )
        );

        JLabel titleLabel =
                new JLabel(
                        ticket.getTitle()
                );

        titleLabel.setFont(
                titleLabel.getFont()
                        .deriveFont(Font.BOLD)
        );

        JLabel idLabel =
                new JLabel(
                        "Ticket #" +
                                ticket.getTicketID()
                );

        JLabel priorityLabel =
                new JLabel(
                        "Priority: " +
                                ticket.getPriority()
                );

        JLabel categoryLabel =
                new JLabel(
                        "Category: " +
                                ticket.getCategory()
                );

        JLabel technicianLabel =
                new JLabel(
                        ticket.getAssignedTechnician()
                                .isBlank()
                                ? "Unassigned"
                                : ticket.getAssignedTechnician()
                );

        add(titleLabel);
        add(Box.createVerticalStrut(5));
        add(idLabel);
        add(priorityLabel);
        add(categoryLabel);
        add(technicianLabel);
    }

    public void addCardClickListener(
            Runnable action
    ) {

        MouseListener listener =
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        action.run();
                    }
                };

        addMouseListener(listener);

        for (Component component : getComponents()) {

            component.addMouseListener(
                    listener
            );
        }
    }

    public Ticket getTicket() {

        return ticket;
    }
}