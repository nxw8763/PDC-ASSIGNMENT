package gui.admin;

import model.Admin;
import model.Status;
import model.Ticket;
import service.TicketManagementService;

import javax.swing.*;
import java.awt.*;

public class AdminTicketDetailsDialog extends JDialog {

    private final Ticket ticket;
    private final Admin admin;
    private final TicketManagementService service;

    private JTextArea area;

    public AdminTicketDetailsDialog(
            Window owner,
            Ticket ticket,
            Admin admin,
            TicketManagementService service
    ) {

        super(
                owner,
                "Ticket Details",
                ModalityType.APPLICATION_MODAL
        );

        this.ticket = ticket;
        this.admin = admin;
        this.service = service;

        initialise();
    }

    private void initialise() {

        setLayout(new BorderLayout());

        area = new JTextArea();
        area.setEditable(false);

        refreshText();

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        JButton closeButton =
                new JButton("Close Ticket");

        JButton reopenButton =
                new JButton("Reopen");

        buttons.add(closeButton);
        buttons.add(reopenButton);

        closeButton.addActionListener(
                e -> closeTicket()
        );

        reopenButton.addActionListener(
                e -> reopenTicket()
        );

        add(
                new JScrollPane(area),
                BorderLayout.CENTER
        );

        add(
                buttons,
                BorderLayout.SOUTH
        );

        setSize(500, 350);
        setLocationRelativeTo(getOwner());
    }

    private void refreshText() {

        area.setText(
                "Ticket ID: " + ticket.getTicketID() + "\n\n" +
                "Title: " + ticket.getTitle() + "\n" +
                "Category: " + ticket.getCategory() + "\n" +
                "Priority: " + ticket.getPriority() + "\n" +
                "Status: " + ticket.getStatus() + "\n"
        );
    }

    private void closeTicket() {

        service.closeTicket(
                ticket.getTicketID(),
                admin
        );

        ticket.setStatus(
                Status.CLOSED
        );

        refreshText();
    }

    private void reopenTicket() {

        service.updateStatus(
                ticket.getTicketID(),
                Status.OPEN,
                admin
        );

        ticket.setStatus(
                Status.OPEN
        );

        refreshText();
    }
}