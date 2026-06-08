package gui.employee;

import controller.TicketController;
import model.users.Employee;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyTicketsPanel
        extends JPanel
        implements MyTicketsView {

    private final Employee employee;

    private final TicketController controller;

    private JPanel cardsPanel;

    private JComboBox<String> sortBox;

    public MyTicketsPanel(
            Employee employee,
            TicketController controller) {

        this.employee = employee;

        this.controller = controller;

        initialise();
    }

    private void initialise() {

        setLayout(
                new BorderLayout(
                        15,
                        15
                )
        );

        JPanel topPanel =
                new JPanel(
                        new BorderLayout()
                );

        JLabel title =
                new JLabel(
                        "My Tickets"
                );

        title.setFont(
                title.getFont()
                        .deriveFont(
                                Font.BOLD,
                                24f
                        )
        );

        topPanel.add(
                title,
                BorderLayout.WEST
        );

        sortBox =
                new JComboBox<>(
                        new String[] {
                                "Newest First",
                                "Oldest First",
                                "Priority",
                                "Status",
                                "Category"
                        }
                );

        topPanel.add(
                sortBox,
                BorderLayout.EAST
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        cardsPanel =
                new JPanel(
                        new GridLayout(
                                0,
                                3,
                                15,
                                15
                        )
                );

        JScrollPane scrollPane =
                new JScrollPane(
                        cardsPanel
                );

        scrollPane
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        sortBox.addActionListener(
                e -> controller.loadEmployeeTickets(
                        employee,
                        this,
                        this
                )
        );

        controller.loadEmployeeTickets(
                employee,
                this,
                this
        );
    }

    @Override
    public void setVisible(
            boolean visible) {

        super.setVisible(
                visible
        );

        if (visible) {

            controller.loadEmployeeTickets(
                    employee,
                    this,
                    this
            );
        }
    }

    @Override
    public String getSelectedSort() {

        return (String)
                sortBox.getSelectedItem();
    }

    @Override
    public void displayTickets(
            List<Component> cards) {

        cardsPanel.removeAll();

        for (Component card : cards) {

            cardsPanel.add(card);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
}