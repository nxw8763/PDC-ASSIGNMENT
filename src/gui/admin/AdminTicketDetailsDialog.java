package gui.admin;

import model.Ticket;
import service.TicketManagementService;

import javax.swing.*;
import java.awt.*;

public class AdminTicketDetailsDialog extends JDialog {

    public AdminTicketDetailsDialog(
            Window owner,
            Ticket ticket,
            TicketManagementService service
    ) {

        super(owner, "Ticket Details", ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.setText(
                ticket.getTicketID() + "\n" +
                ticket.getTitle() + "\n" +
                ticket.getStatus() + "\n" +
                ticket.getPriority() + "\n" +
                ticket.getCategory()
        );

        JButton close = new JButton("Close Ticket");

        close.addActionListener(e -> {
            service.closeTicket(ticket.getTicketID());
            dispose();
        });

        add(new JScrollPane(area), BorderLayout.CENTER);
        add(close, BorderLayout.SOUTH);

        setSize(400,300);
        setLocationRelativeTo(owner);
    }
}