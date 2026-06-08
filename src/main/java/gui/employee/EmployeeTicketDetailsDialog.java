package gui.employee;

import gui.abstracts.AbstractTicketDetailsDialog;

import javax.swing.*;
import java.awt.*;

public class EmployeeTicketDetailsDialog
        extends AbstractTicketDetailsDialog {

    public EmployeeTicketDetailsDialog(
            Window owner,
            int ticketId) {

        super(
                owner,
                ticketId
        );
    }

    @Override
    protected JPanel createActionPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        commentButton =
                new JButton(
                        "Add Comment"
                );

        panel.add(
                commentButton
        );

        return panel;
    }
}