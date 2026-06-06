package model;

public class Technician extends User {

    private Ticket currentTicket;

    public Technician(int userID, String username,String name, String email) {
        super(userID, username,name, email, "TECHNICIAN");
    }

    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    public void setCurrentTicket(Ticket ticket) {
        this.currentTicket = ticket;
    }
    
    @Override
    public String toString() {
        return getUserID() + " - " + getEmail();
    }
}