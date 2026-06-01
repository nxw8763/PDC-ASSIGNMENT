package service;

import database.TicketDatabase;
import database.UserDatabase;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketManagementService {

    private TicketDatabase database = new TicketDatabase();
    private UserDatabase userDatabase;
    
    public TicketManagementService(UserDatabase userdatabase) {
    	this.userDatabase = userdatabase;
    }

    public Ticket createTicket(String title, String desc,
		            String category,
		            Priority priority,
		            User creator) {
		
		int id = (int)(Math.random() * 10000);
		
		Ticket ticket = new Ticket(
			id,
			title,
			desc,
			category,
			priority,
			creator.getUserID(),
			Status.OPEN,
			LocalDateTime.now()
		);
		
		database.saveTicket(ticket);
		
		return ticket;
    }

    public List<Ticket> getOpenTicketsByUser(int userID) {

        List<Ticket> allTickets = database.fetchTickets();
        List<Ticket> result = new ArrayList<>();

        for (Ticket t : allTickets) {

            if (t.getStatus() != Status.CLOSED &&
                t.getCreatedByUserID() == userID) {

                result.add(t);
            }
        }

        return result;
    }
    
    public List<Ticket> getTicketsByTechnician(String techEmail) {

        List<Ticket> result = new ArrayList<>();

        for (Ticket t : database.fetchTickets()) {

        	if (t.getAssignedTechnician().equals(techEmail)) {

                result.add(t);
            }
        }

        return result;
    }
    
    public List<Ticket> getAllTickets() {
        return database.fetchTickets();
    }
    
    public List<Ticket> getUnassignedTickets() {

        List<Ticket> result = new ArrayList<>();

        for (Ticket t : database.fetchTickets()) {

            if (t.getAssignedTechnician().equals("") &&
                t.getStatus() == Status.OPEN) {

                result.add(t);
            }
        }

        return result;
    }
    
    public void assignTicketToTechnician(int ticketID, Technician tech) {

        List<Ticket> tickets = database.fetchTickets();

        for (Ticket t : tickets) {

            if (t.getTicketID() == ticketID) {
                t.assignTechnician(tech.getEmail());
                t.setStatus(Status.ASSIGNED);
                database.updateTicket(t);
                return;
            }
        }
    }
    
    public void assignTicketByAdmin(int ticketID, int technicianIndex) {

        List<Ticket> tickets = database.fetchTickets();
        List<User> users = userDatabase.fetchUsers();

        // 1. Filter technicians only
        List<Technician> technicians = new ArrayList<>();

        for (User u : users) {

            if (u instanceof Technician) {
                technicians.add((Technician) u);
            }
        }

        if (technicianIndex < 0 || technicianIndex >= technicians.size()) {
            System.out.println("Invalid technician selection.");
            return;
        }

        Technician selectedTech = technicians.get(technicianIndex);

        // 2. Find ticket
        for (Ticket t : tickets) {

            if (t.getTicketID() == ticketID) {

                if (t.getStatus() == Status.CLOSED) {
                    System.out.println("Cannot assign a closed ticket.");
                    return;
                }

                t.assignTechnician(selectedTech.getEmail());
                t.setStatus(Status.ASSIGNED);

                database.updateTicket(t);

                System.out.println("Ticket assigned to " + selectedTech.getName());
                return;
            }
        }

        System.out.println("Ticket not found.");
    }
    
    
    public void updateTicketStatus(int ticketID, Status status, Technician technician) {

        List<Ticket> tickets = database.fetchTickets();

        for (Ticket t : tickets) {

            if (t.getTicketID() == ticketID) {
        	   if (!technicianCanModifyTicket(t, technician)) {
        	        return;
        	    }
            	t.setStatus(status);
                database.updateTicket(t);
                return;
            }
        }
    }
    
    public void updateTicketPriority(int ticketID, Priority priority, Technician technician) {

        List<Ticket> tickets = database.fetchTickets();

        for (Ticket t : tickets) {

            if (t.getTicketID() == ticketID) {
        	   if (!technicianCanModifyTicket(t, technician)) {
        	        return;
        	    }
                t.setPriority(priority);

                database.updateTicket(t);

                return;
            }
        }
    }
    
    public void closeTicket(int ticketID) {

        List<Ticket> tickets = database.fetchTickets();

        for (Ticket t : tickets) {

            if (t.getTicketID() == ticketID) {
                t.setStatus(Status.CLOSED);
                database.updateTicket(t);
                return;
            }
        }
    }
    
    public void generateReport() {

        List<Ticket> tickets = database.fetchTickets();

        int open = 0, assigned = 0, inProgress = 0, resolved = 0, closed = 0;

        Map<Priority, Integer> priorityCount = new HashMap<>();
        Map<String, Integer> technicianWorkload = new HashMap<>();
        Map<Integer, Integer> ticketsPerUser = new HashMap<>();

        System.out.println("\n========== ACTIVE TICKETS ==========");

        for (Ticket t : tickets) {

            // Skip closed tickets for active list
            if (t.getStatus() == Status.CLOSED) {
                closed++;
            } else {

                System.out.println(
                        t.getTicketID() + " | " +
                        t.getTitle() + " | " +
                        t.getCategory() + " | " +
                        t.getStatus() + " | " +
                        t.getPriority()
                );
            }

            // STATUS COUNT
            switch (t.getStatus()) {

                case OPEN -> open++;
                case ASSIGNED -> assigned++;
                case IN_PROGRESS -> inProgress++;
                case RESOLVED -> resolved++;
                case CLOSED -> closed++;
            }

            // PRIORITY COUNT
            priorityCount.put(
                    t.getPriority(),
                    priorityCount.getOrDefault(t.getPriority(), 0) + 1
            );

            // TECHNICIAN WORKLOAD (EMAIL BASED)
            if (t.getAssignedTechnician() != null && !t.getAssignedTechnician().isEmpty()) {

                technicianWorkload.put(
                        t.getAssignedTechnician(),
                        technicianWorkload.getOrDefault(t.getAssignedTechnician(), 0) + 1
                );
            }

            // USER WORKLOAD (CREATOR)
            ticketsPerUser.put(
                    t.getCreatedByUserID(),
                    ticketsPerUser.getOrDefault(t.getCreatedByUserID(), 0) + 1
            );
        }

        // =========================
        // SUMMARY SECTION
        // =========================

        System.out.println("\n========== STATUS COUNTS ==========");
        System.out.println("OPEN: " + open);
        System.out.println("ASSIGNED: " + assigned);
        System.out.println("IN_PROGRESS: " + inProgress);
        System.out.println("RESOLVED: " + resolved);
        System.out.println("CLOSED: " + closed);

        System.out.println("\n========== PRIORITY COUNTS ==========");
        for (Priority p : priorityCount.keySet()) {
            System.out.println(p + ": " + priorityCount.get(p));
        }

        System.out.println("\n========== OPEN vs CLOSED ==========");
        System.out.println("OPEN TICKETS: " + (open + assigned + inProgress + resolved));
        System.out.println("CLOSED TICKETS: " + closed);

        System.out.println("\n========== TECHNICIAN WORKLOAD ==========");
        for (String email : technicianWorkload.keySet()) {
            System.out.println(email + " → " + technicianWorkload.get(email) + " tickets");
        }

        System.out.println("\n========== TICKETS PER USER ==========");
        for (Integer userId : ticketsPerUser.keySet()) {
            System.out.println("User " + userId + " → " + ticketsPerUser.get(userId));
        }
    }
    
    public void addComment(int ticketID, String title, String desc, String email) {

        List<Ticket> tickets = database.fetchTickets();

        for (Ticket t : tickets) {

            if (t.getTicketID() == ticketID) {

                Comment comment = new Comment(
                        title,
                        desc,
                        email,
                        LocalDateTime.now()
                );

                t.addComment(comment);
                database.updateTicket(t);
                return;
            }
        }
    }
    
    public void commentTicket(Ticket ticket, Comment comment) {

        ticket.addComment(comment);

        database.updateTicket(ticket); // ✔ overwrite instead of append
    }
    
    private boolean technicianCanModifyTicket(Ticket ticket, User technician) {

        if (ticket.getStatus() == Status.CLOSED) {
            System.out.println("Cannot modify a closed ticket.");
            return false;
        }

        if (ticket.getAssignedTechnician() == null || ticket.getAssignedTechnician().isEmpty()) {
            System.out.println("Ticket is not assigned to any technician.");
            return false;
        }

        if (!ticket.getAssignedTechnician().equalsIgnoreCase(technician.getEmail())) {
            System.out.println("You can only modify tickets assigned to you.");
            return false;
        }

        return true;
    }
}