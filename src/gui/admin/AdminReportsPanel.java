package gui.admin;

import service.TicketManagementService;

import javax.swing.*;

import model.Admin;

import java.awt.*;

public class AdminReportsPanel extends JPanel {

    public AdminReportsPanel(Admin admin, TicketManagementService service) {

        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        JButton refresh = new JButton("Generate Report");

        refresh.addActionListener(e -> {
            area.setText(generate(admin, service));
        });

        add(refresh, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    private String generate(Admin admin, TicketManagementService service) {

        StringBuilder sb = new StringBuilder();

        service.getVisibleTickets(admin).forEach(t -> {

            sb.append(t.getTicketID()).append(" | ")
              .append(t.getTitle()).append(" | ")
              .append(t.getStatus()).append(" | ")
              .append(t.getPriority())
              .append("\n");
        });

        return sb.toString();
    }
}