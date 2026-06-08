package gui.technician;

import gui.abstracts.AbstractTicketDetailsDialog;

import javax.swing.*;
import java.awt.*;

public class TechnicianTicketDetailsDialog
        extends AbstractTicketDetailsDialog {

    private JButton assignButton;
    private JButton unassignButton;
    private JButton statusButton;
    private JButton priorityButton;
    private JButton commentButton;

    public TechnicianTicketDetailsDialog(
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

        assignButton =
                new JButton(
                        "Assign To Me"
                );

        unassignButton =
                new JButton(
                        "Unassign"
                );

        statusButton =
                new JButton(
                        "Change Status"
                );

        priorityButton =
                new JButton(
                        "Change Priority"
                );

        commentButton =
                new JButton(
                        "Add Comment"
                );

        panel.add(assignButton);
        panel.add(unassignButton);
        panel.add(statusButton);
        panel.add(priorityButton);
        panel.add(commentButton);

        return panel;
    }

    public void setAssignAction(
            Runnable action) {

        assignButton.addActionListener(
                e -> action.run()
        );
    }

    public void setUnassignAction(
            Runnable action) {

        unassignButton.addActionListener(
                e -> action.run()
        );
    }

    public void setStatusAction(
            Runnable action) {

        statusButton.addActionListener(
                e -> action.run()
        );
    }

    public void setPriorityAction(
            Runnable action) {

        priorityButton.addActionListener(
                e -> action.run()
        );
    }

    public void setCommentAction(
            Runnable action) {

        commentButton.addActionListener(
                e -> action.run()
        );
    }
}