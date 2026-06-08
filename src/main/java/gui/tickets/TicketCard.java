package gui.tickets;

import dto.TicketCardDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class TicketCard extends JPanel {

    private final TicketCardDTO ticket;

    public TicketCard(
            TicketCardDTO ticket,
            Runnable onClick
    ) {

        this.ticket = ticket;

        setLayout(new BorderLayout(10, 10));

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );

        setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        JPanel content = new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(
                        ticket.getTitle()
                );

        titleLabel.setFont(
                titleLabel.getFont()
                        .deriveFont(
                                Font.BOLD,
                                16f
                        )
        );

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(5));

        content.add(
                new JLabel(
                        "Ticket #" +
                                ticket.getTicketId()
                )
        );

        content.add(
                new JLabel(
                        "Priority: " +
                                ticket.getPriority()
                )
        );

        content.add(
                new JLabel(
                        "Category: " +
                                ticket.getCategory()
                )
        );

        content.add(
                new JLabel(
                        "Technician Email: " +
                                (
                                        ticket.getAssignedTechnician() == null
                                                ? "Unassigned"
                                                : ticket.getAssignedTechnician()
                                )
                )
        );

        add(
                content,
                BorderLayout.CENTER
        );

        MouseAdapter listener =
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {
                        onClick.run();
                    }
                };

        addMouseListener(listener);

        for (Component c : content.getComponents()) {
            c.addMouseListener(listener);
        }
    }

    public TicketCardDTO getTicket() {
        return ticket;
    }
}