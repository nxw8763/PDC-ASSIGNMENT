package gui.employee;

import model.Employee;
import model.Ticket;
import service.TicketManagementService;
import gui.tickets.TicketCard;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class MyTicketsPanel extends JPanel {

    private final Employee employee;
    private final TicketManagementService ticketService;

    private JPanel cardsPanel;
    private JComboBox<String> sortBox;

    public MyTicketsPanel(Employee employee,
                          TicketManagementService ticketService) {

        this.employee = employee;
        this.ticketService = ticketService;

        initialise();
    }

    private void initialise() {

        setLayout(new BorderLayout(15, 15));

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("My Tickets");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));

        topPanel.add(title, BorderLayout.WEST);

        sortBox = new JComboBox<>(new String[]{
                "Newest First",
                "Oldest First",
                "Priority",
                "Status",
                "Category"
        });

        topPanel.add(sortBox, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(0, 3, 15, 15));

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        sortBox.addActionListener(e -> loadTickets());

        loadTickets();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) loadTickets();
    }

    private void loadTickets() {

        cardsPanel.removeAll();

        List<Ticket> tickets =
                ticketService.getVisibleTickets(employee);

        applySorting(tickets);

        for (Ticket t : tickets) {

        	cardsPanel.add(
        		    new TicketCard(
        		            t,
        		            () -> openTicket(t)
        		    )
        		);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void applySorting(List<Ticket> tickets) {

        String sort = (String) sortBox.getSelectedItem();

        switch (sort) {

            case "Newest First" ->
                    tickets.sort(Comparator.comparing(Ticket::getCreatedDate).reversed());

            case "Oldest First" ->
                    tickets.sort(Comparator.comparing(Ticket::getCreatedDate));

            case "Priority" ->
                    tickets.sort((a, b) ->
                            Integer.compare(
                                    b.getPriority().ordinal(),
                                    a.getPriority().ordinal()
                            )
                    );

            case "Status" ->
                    tickets.sort(Comparator.comparing(Ticket::getStatus));

            case "Category" ->
                    tickets.sort(Comparator.comparing(Ticket::getCategory));
        }
    }

    private void openTicket(Ticket ticket) {

        Window window = SwingUtilities.getWindowAncestor(this);

        EmployeeTicketDetailsDialog dialog =
                new EmployeeTicketDetailsDialog(
                        window,
                        ticket,
                        employee,
                        ticketService
                );

        dialog.setVisible(true);
        loadTickets();
    }
}