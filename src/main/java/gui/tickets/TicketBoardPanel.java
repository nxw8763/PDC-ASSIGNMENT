package gui.tickets;

import controller.TicketController;
import model.users.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TicketBoardPanel
        extends JPanel
        implements TicketBoardView {

    private final User currentUser;

    private final TicketController controller;

    private final KanbanColumnPanel openColumn;

    private final KanbanColumnPanel assignedColumn;

    private final KanbanColumnPanel progressColumn;

    private final KanbanColumnPanel resolvedColumn;

    public TicketBoardPanel(
            User currentUser,
            TicketController controller) {

        this.currentUser =
                currentUser;

        this.controller =
                controller;

        setLayout(
                new BorderLayout()
        );

        JPanel boardPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                0
                        )
                );

        openColumn =
                new KanbanColumnPanel(
                        "OPEN"
                );

        assignedColumn =
                new KanbanColumnPanel(
                        "ASSIGNED"
                );

        progressColumn =
                new KanbanColumnPanel(
                        "IN PROGRESS"
                );

        resolvedColumn =
                new KanbanColumnPanel(
                        "RESOLVED"
                );

        boardPanel.add(openColumn);
        boardPanel.add(assignedColumn);
        boardPanel.add(progressColumn);
        boardPanel.add(resolvedColumn);

        add(
                new JScrollPane(
                        boardPanel
                ),
                BorderLayout.CENTER
        );

        controller.loadBoard(
                currentUser,
                this,
                this
        );
    }

    public void refreshBoard() {

        controller.loadBoard(
                currentUser,
                this,
                this
        );
    }

    @Override
    public void displayOpenTickets(
            List<Component> cards) {

        openColumn.clearCards();

        for (Component card : cards) {

            openColumn.addCard(
                    (TicketCard) card
            );
        }
    }

    @Override
    public void displayAssignedTickets(
            List<Component> cards) {

        assignedColumn.clearCards();

        for (Component card : cards) {

            assignedColumn.addCard(
                    (TicketCard) card
            );
        }
    }

    @Override
    public void displayInProgressTickets(
            List<Component> cards) {

        progressColumn.clearCards();

        for (Component card : cards) {

            progressColumn.addCard(
                    (TicketCard) card
            );
        }
    }

    @Override
    public void displayResolvedTickets(
            List<Component> cards) {

        resolvedColumn.clearCards();

        for (Component card : cards) {

            resolvedColumn.addCard(
                    (TicketCard) card
            );
        }
    }
}