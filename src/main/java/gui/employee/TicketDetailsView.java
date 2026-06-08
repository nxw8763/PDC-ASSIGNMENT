package gui.employee;

import dto.TicketDetailsDTO;

public interface TicketDetailsView {

    int getTicketId();

    void displayTicket(
            TicketDetailsDTO dto
    );

    void showMessage(
            String message
    );

    void showError(
            String message
    );

    void setAddCommentAction(
            Runnable action
    );
}