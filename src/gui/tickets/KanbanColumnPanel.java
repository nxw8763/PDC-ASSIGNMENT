package gui.tickets;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class KanbanColumnPanel extends JPanel {

    private final JPanel cardsPanel;

    public KanbanColumnPanel(String title) {

        setLayout(new BorderLayout());

        setBorder(
                BorderFactory.createLineBorder(
                        Color.LIGHT_GRAY
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        add(titleLabel, BorderLayout.NORTH);

        cardsPanel = new JPanel();

        cardsPanel.setLayout(
                new BoxLayout(
                        cardsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        cardsPanel.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(cardsPanel);

        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void addCard(TicketCard card) {

        cardsPanel.add(card);
        cardsPanel.add(
                Box.createVerticalStrut(10)
        );
    }

    public void clearCards() {

        cardsPanel.removeAll();
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
}