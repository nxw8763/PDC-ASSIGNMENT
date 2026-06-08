package gui.employee;

import java.awt.Component;
import java.util.List;

public interface MyTicketsView {

    String getSelectedSort();

    void displayTickets(List<Component> cards);
}