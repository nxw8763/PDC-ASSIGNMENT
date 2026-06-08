package gui.employee;

import model.enums.Priority;

public interface CreateTicketView {

    String getTicketTitle();

    String getTicketDescription();

    String getSelectedCategory();

    Priority getSelectedPriority();

    void showMessage(String message);

    void showError(String message);

    void clearForm();
}