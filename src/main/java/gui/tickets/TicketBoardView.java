package gui.tickets;

import java.awt.Component;
import java.util.List;

public interface TicketBoardView {

    void displayOpenTickets(
            List<Component> cards
    );

    void displayAssignedTickets(
            List<Component> cards
    );

    void displayInProgressTickets(
            List<Component> cards
    );

    void displayResolvedTickets(
            List<Component> cards
    );
}