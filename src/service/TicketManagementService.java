package service;

import dao.TicketDAO;
import model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TicketManagementService {

    private final TicketDAO ticketDAO;

    public TicketManagementService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    // =========================
    // CREATE
    // =========================
    public Ticket createTicket(String title, String desc,
                               String category, Priority priority,
                               User creator) {

        Ticket t = new Ticket(
                0,
                title,
                desc,
                category,
                priority,
                creator.getUserID(),
                Status.OPEN,
                LocalDateTime.now()
        );

        int id = ticketDAO.saveTicket(t);
        t.setTicketID(id);

        return t;
    }

    
    
    // =========================
    // READ
    // =========================
    public List<Ticket> getVisibleTickets(User user) {

        if (user instanceof Admin admin) {
            return ticketDAO.fetchAllTickets();
        }

        if (user instanceof Technician tech) {
            return ticketDAO.fetchOpenOrAssignedTickets(tech.getUserID());
        }

        if (user instanceof Employee emp) {
            return ticketDAO.fetchTicketsCreatedBy(emp.getUserID());
        }

        return List.of();
    }
    
    

    // =========================
    // ASSIGN / UNASSIGN
    // =========================
    public void assignTicket(int ticketID, int techID, String techEmail, User actor) {

        Ticket t = ticketDAO.getTicketByID(ticketID);
        if (t == null) return;

        if (!canModifyTicket(t, actor)) return;

        t.setAssignedTechnicianID(techID);
        t.setAssignedTechnicianEmail(techEmail);
        t.setStatus(Status.ASSIGNED);

        ticketDAO.updateTicket(t);
    }

    public void unassignTicket(int ticketID, User actor) {

        Ticket t = ticketDAO.getTicketByID(ticketID);
        if (t == null) return;

        if (!canModifyTicket(t, actor)) return;

        t.setAssignedTechnicianID(0);
        t.setAssignedTechnicianEmail(null);
        t.setStatus(Status.OPEN);

        ticketDAO.updateTicket(t);
    }

    // =========================
    // STATUS (FIXED)
    // =========================
    public void updateStatus(int ticketID, Status status, User actor) {

        Ticket t = ticketDAO.getTicketByID(ticketID);
        if (t == null) return;

        if (!canModifyTicket(t, actor)) return;

        t.setStatus(status);
        ticketDAO.updateTicket(t);
    }

    // =========================
    // PRIORITY (FIXED)
    // =========================
    public void updatePriority(int ticketID, Priority priority, User actor) {

        Ticket t = ticketDAO.getTicketByID(ticketID);
        if (t == null) return;

        if (!canModifyTicket(t, actor)) return;

        t.setPriority(priority);
        ticketDAO.updateTicket(t);
    }

    // =========================
    // CLOSE
    // =========================
    public void closeTicket(int ticketID, User actor) {

        updateStatus(ticketID, Status.CLOSED, actor);
    }

    // =========================
    // COMMENTS
    // =========================
    public void addComment(int ticketID, String title,
                           String desc, String user) {

        ticketDAO.addComment(
                ticketID,
                new Comment(title, desc, user, LocalDateTime.now())
        );
    }

    // =========================
    // SECURITY CORE LOGIC
    // =========================
    private boolean canModifyTicket(Ticket t, User actor) {

        if (actor instanceof Admin) {
            return true; // admins can do everything
        }

        if (actor instanceof Technician tech) {

            if (t.getStatus() == Status.CLOSED) {
                return false;
            }

            if (t.getAssignedTechnicianID() == 0) {
                return true; // can assign self
            }

            return t.getAssignedTechnicianID() == tech.getUserID();
        }

        // employees cannot modify tickets
        return false;
    }
}