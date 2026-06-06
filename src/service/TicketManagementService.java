package service;

import dao.TicketDAO;

import model.*;
import model.enums.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TicketManagementService {

    private final TicketDAO ticketDAO;

    public TicketManagementService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

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

        AuditService.addAuditLog(creator.getUserID(), AuditAction.CREATE, AuditEntity.TICKET, t.getTicketID(), creator.getName() + " created ticket ID:" + t.getTicketID() + " '" + title + "'");;
        
        return t;
    }

    public List<Ticket> getVisibleTickets(User user) {

        if (user instanceof Admin admin) {
        	List<Ticket> tickets = ticketDAO.fetchAllTickets();
        	AuditService.addAuditLog(admin.getUserID(), AuditAction.FETCH, AuditEntity.TICKET, tickets.size(), admin.getName() + " fetched " + tickets.size() + " tickets");
        	return tickets;
        }

        if (user instanceof Technician tech) {
        	List<Ticket> tickets = ticketDAO.fetchOpenOrAssignedTickets(tech.getUserID());
        	AuditService.addAuditLog(tech.getUserID(), AuditAction.FETCH, AuditEntity.TICKET, tickets.size(), tech.getName() + " fetched " + tickets.size() + " tickets");
            return tickets;
        }

        if (user instanceof Employee emp) {
            List<Ticket> tickets = ticketDAO.fetchTicketsCreatedBy(emp.getUserID());
            AuditService.addAuditLog(emp.getUserID(), AuditAction.FETCH, AuditEntity.TICKET, tickets.size(), emp.getName() + " fetched " + tickets.size() + " tickets");
            return tickets;
        }

        return List.of();
    }
    
    public void assignTicket(int ticketID, int techID, String techEmail, User user) {

        Ticket t = ticketDAO.getTicketByID(ticketID);
        if (t == null) return;

        if (!canModifyTicket(t, user)) return;

        t.setAssignedTechnicianID(techID);
        t.setAssignedTechnicianEmail(techEmail);
        t.setStatus(Status.ASSIGNED);
        
        AuditService.addAuditLog(user.getUserID(), AuditAction.ASSIGN, AuditEntity.TICKET, ticketID, user.getName() + " assigned " + techEmail + " to ticket #" + ticketID);
        
        ticketDAO.updateTicket(t);
    }

    public void unassignTicket(int ticketID, User user) {

        Ticket t = ticketDAO.getTicketByID(ticketID);
        if (t == null) return;

        if (!canModifyTicket(t, user)) return;

        AuditService.addAuditLog(user.getUserID(), AuditAction.UNASSIGN, AuditEntity.TICKET, ticketID, user.getName() + " unassigned " + t.getAssignedTechnicianEmail() + " from ticket #" + ticketID);

        t.setAssignedTechnicianID(0);
        t.setAssignedTechnicianEmail(null);
        t.setStatus(Status.OPEN);
        
        ticketDAO.updateTicket(t);
    }

    public void updateStatus(int ticketID, Status status, User user) {

        Ticket t = ticketDAO.getTicketByID(ticketID);
        if (t == null) return;

        if (!canModifyTicket(t, user)) return;

        AuditService.addAuditLog(user.getUserID(), AuditAction.STATUS, AuditEntity.TICKET, ticketID, user.getName() + " changed ticket #" + t.getTicketID() + " status from "+ t.getStatus() + " to " + status);
        
        t.setStatus(status);
        
        ticketDAO.updateTicket(t);
    }

    public void updatePriority(int ticketID, Priority priority, User user) {

        Ticket t = ticketDAO.getTicketByID(ticketID);
        if (t == null) return;

        if (!canModifyTicket(t, user)) return;

        AuditService.addAuditLog(user.getUserID(), AuditAction.PRIORITY, AuditEntity.TICKET, ticketID, user.getName() + " changed ticket #" + t.getTicketID() + " priortiy from "+ t.getPriority() + " to " + priority);
        
        t.setPriority(priority);
        ticketDAO.updateTicket(t);
    }

    public void closeTicket(int ticketID, User user) {
    	
    	AuditService.addAuditLog(user.getUserID(), AuditAction.CLOSE, AuditEntity.TICKET, ticketID, user.getName() + " closed ticket #" + ticketID);
        updateStatus(ticketID, Status.CLOSED, user);
    }

    public void addComment(int ticketID, String title,
                           String desc, User user) {
    	
    	AuditService.addAuditLog(user.getUserID(), AuditAction.COMMENT, AuditEntity.TICKET, ticketID, user.getName() + " commented on ticket #" + ticketID + " '" + title + "'");

        ticketDAO.addComment(
                ticketID,
                new Comment(title, desc, user.getEmail(), LocalDateTime.now())
        );
    }

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