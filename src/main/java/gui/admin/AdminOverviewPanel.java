package gui.admin;

import service.TicketManagementService;

import javax.swing.*;

import model.Admin;

import java.awt.*;

public class AdminOverviewPanel extends JPanel {
	
    public AdminOverviewPanel(Admin admin, TicketManagementService service) {

        setLayout(new GridLayout(3, 2, 10, 10));

        JButton refresh = new JButton("Refresh Overview");

        JTextArea area = new JTextArea();
        area.setEditable(false);

        refresh.addActionListener(e -> {
            area.setText(generate(admin, service));
        });

        add(refresh);
        add(new JScrollPane(area));
    }

    private String generate(Admin admin, TicketManagementService service) {
        StringBuilder sb = new StringBuilder();

        service.getVisibleTickets(admin).forEach(t -> {
            sb.append(t.getTicketID())
              .append(" | ")
              .append(t.getTitle())
              .append(" | ")
              .append(t.getStatus())
              .append("\n");
        });

        return sb.toString();
    }
}