package gui.admin;

import model.Ticket;
import service.TicketManagementService;

import javax.swing.*;
import java.awt.*;

public class AdminTicketsPanel extends JPanel {

    private final TicketManagementService service;
    private final DefaultListModel<Ticket> model = new DefaultListModel<>();
    private final JList<Ticket> list = new JList<>(model);

    public AdminTicketsPanel(TicketManagementService service) {

        this.service = service;

        setLayout(new BorderLayout());

        JButton refresh = new JButton("Refresh");
        JButton open = new JButton("Open");

        JPanel top = new JPanel();
        top.add(refresh);
        top.add(open);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        refresh.addActionListener(e -> load());
        open.addActionListener(e -> open());
    }

    private void load() {
        model.clear();
        service.getAllTickets().forEach(model::addElement);
    }

    private void open() {

        Ticket t = list.getSelectedValue();
        if (t == null) return;

        new AdminTicketDetailsDialog(
                SwingUtilities.getWindowAncestor(this),
                t,
                service
        ).setVisible(true);
    }
}