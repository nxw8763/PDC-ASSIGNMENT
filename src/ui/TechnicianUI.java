package ui;

import model.*;
import service.TicketManagementService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import database.UserDatabase;

public class TechnicianUI {

    private TicketManagementService ticketService;
    private Scanner scanner = new Scanner(System.in);

    public void start(Technician tech, Scanner scanner, UserDatabase userdatabase) {
    	this.scanner = scanner;
    	this.ticketService = new TicketManagementService(userdatabase);
        while (true) {

        	System.out.println("\n=== Technician Menu ===");
        	System.out.println("1. View Unassigned Tickets");
        	System.out.println("2. View My Tickets");
        	System.out.println("3. Assign Ticket to Me");
        	System.out.println("4. Update Ticket Status");
        	System.out.println("5. Change Ticket Priority");
        	System.out.println("6. Comment on Ticket");
        	System.out.println("7. Close Ticket");
        	System.out.println("8. Logout");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    viewUnassigned();
                    break;

                case "2":
                    viewMyTickets(tech);
                    break;

                case "3":
                    assignTicket(tech);
                    break;

                case "4":
                    updateStatus(tech);
                    break;
                case "5":
                    updatePriority(tech);
                    break;
                case "6":
                    commentTicket(tech);
                    break;

                case "7":
                    closeTicket(tech);
                    break;

                case "8":
                    return;
            }
        }
    }

    private void viewUnassigned() {

        List<Ticket> tickets = ticketService.getUnassignedTickets();

        for (int i = 0; i < tickets.size(); i++) {

            Ticket t = tickets.get(i);

            System.out.println((i + 1) + ". " +
                    t.getTicketID() + " | " +
                    t.getTitle() + " | " +
                    t.getPriority());
        }
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return; // automatically backs out
        }
    }
    
    private void viewMyTickets(Technician tech) {

        List<Ticket> tickets =
                ticketService.getTicketsByTechnician(tech.getEmail());

        for (int i = 0; i < tickets.size(); i++) {

            Ticket t = tickets.get(i);

            t.printTicket();
        }
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return; // automatically backs out
        }
    }

    private void assignTicket(Technician tech) {

        List<Ticket> tickets = ticketService.getUnassignedTickets();

        for (int i = 0; i < tickets.size(); i++) {

            Ticket t = tickets.get(i);
            if(t.getStatus() == Status.OPEN) {
            System.out.println((i + 1) + ". " +
                    t.getTicketID() + " | " +
                    t.getTitle());
            }
        }
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return; // automatically backs out
        }
        
        int choice;
        while(true) {
        	System.out.print("Select ticket number: ");
        	choice = getSafeIntInput(scanner);
        	if (choice >= 1 && choice <= tickets.size()) {
                break;
            }

            System.out.println("Invalid option.");
        }
        Ticket selected = tickets.get(choice - 1);

        ticketService.assignTicketToTechnician(
                selected.getTicketID(),
                tech
        );

        System.out.println("Ticket assigned.");
    }
    
    private void updatePriority(Technician tech) {

        List<Ticket> tickets = ticketService.getAllTickets();

        for (int i = 0; i < tickets.size(); i++) {

            Ticket t = tickets.get(i);

            System.out.println((i + 1) + ". " +
                    t.getTicketID() + " | " +
                    t.getTitle() + " | " +
                    t.getPriority());
        }
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return; // automatically backs out
        }

        int choice;
        while(true) {
        	System.out.print("Select ticket number: ");
        	choice = getSafeIntInput(scanner);
        	if (choice >= 1 && choice <= tickets.size()) {
                break;
            }

            System.out.println("Invalid option.");
        }
        Ticket selected = tickets.get(choice - 1);

        Priority priority = selectPriority();

        ticketService.updateTicketPriority(
                selected.getTicketID(),
                priority,
                tech
        );

        System.out.println("Priority updated.");
    }

    private Priority selectPriority() {

        while (true) {

            System.out.println("Select Priority");
            System.out.println("1. Low");
            System.out.println("2. Medium");
            System.out.println("3. High");
            System.out.println("4. Critical");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1": return Priority.LOW;
                case "2": return Priority.MEDIUM;
                case "3": return Priority.HIGH;
                case "4": return Priority.CRITICAL;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
    
    private void updateStatus(Technician tech) {

        List<Ticket> tickets = ticketService.getAllTickets();

        for (int i = 0; i < tickets.size(); i++) {

            Ticket t = tickets.get(i);

            System.out.println((i + 1) + ". " +
                    t.getTicketID() + " | " +
                    t.getTitle() + " | " +
                    t.getStatus());
        }
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return; // automatically backs out
        }
        int choice;
        while(true) {
        	System.out.print("Select ticket number: ");
        	choice = getSafeIntInput(scanner);
        	if (choice >= 1 && choice <= tickets.size()) {
                break;
            }

            System.out.println("Invalid option.");
        }
        Ticket selected = tickets.get(choice - 1);

        System.out.println("0. OPEN");
        System.out.println("1. IN_PROGRESS");
        System.out.println("2. RESOLVED");

        String statusChoice = scanner.nextLine();

        Status status = switch (statusChoice) {
        	case "0" -> Status.IN_PROGRESS;
        	case "1" -> Status.IN_PROGRESS;
            case "2" -> Status.RESOLVED;
            default -> Status.IN_PROGRESS;
        };

        ticketService.updateTicketStatus(
                selected.getTicketID(),
                status,
                tech
        );

        System.out.println("Status updated.");
    }

    private void commentTicket(Technician tech) {

        List<Ticket> tickets = ticketService.getAllTickets();

        for (int i = 0; i < tickets.size(); i++) {

            Ticket t = tickets.get(i);
            if(t.getAssignedTechnician().equalsIgnoreCase(tech.getEmail()) || t.getAssignedTechnician().equalsIgnoreCase("") || t.getStatus() == Status.OPEN) {
	            System.out.println((i + 1) + ". " +
	                    t.getTicketID() + " | " +
	                    t.getTitle());
            }
        }
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return; // automatically backs out
        }
        int choice;
        while(true) {
        	System.out.print("Select ticket number: ");
        	choice = getSafeIntInput(scanner);
        	if (choice >= 1 && choice <= tickets.size()) {
                break;
            }

            System.out.println("Invalid option.");
        }
        Ticket selected = tickets.get(choice - 1);

        System.out.print("Comment Title: ");
        String title = getSafeStringInput(scanner);

        System.out.print("Comment Description: ");
        String desc = getSafeStringInput(scanner);

        Comment comment = new Comment(
                title,
                desc,
                tech.getEmail(),
                LocalDateTime.now()
        );

        ticketService.commentTicket(selected, comment);

        System.out.println("Comment added.");
    }

    private void closeTicket(Technician tech) {

        List<Ticket> tickets = ticketService.getAllTickets();

        for (int i = 0; i < tickets.size(); i++) {

            Ticket t = tickets.get(i);
            if(t.getAssignedTechnician().equalsIgnoreCase(tech.getEmail())) {
            System.out.println((i + 1) + ". " +
                    t.getTicketID() + " | " +
                    t.getTitle());
            }
        }
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return; // automatically backs out
        }
        int choice;
        while(true) {
	        System.out.print("Select ticket number: ");
	        choice = getSafeIntInput(scanner);
	        if (choice >= 1 && choice <= tickets.size()) {
	            break;
	        }
	
	        System.out.println("Invalid option.");
        }
        Ticket selected = tickets.get(choice - 1);

        ticketService.closeTicket(selected.getTicketID());

        System.out.println("Ticket closed.");
    }
    
    public static String getSafeStringInput(Scanner scanner) {

        while (true) {

            String input = scanner.nextLine();

            if (input == null || input.trim().isEmpty()) {
                System.out.println("Invalid input. Input cannot be blank.");
                continue;
            }

            if (input.contains("|")) {
                System.out.println("Invalid input. '|' character is not allowed.");
                continue;
            }

            return input.trim();
        }
    }
    
    public static int getSafeIntInput(Scanner scanner) {

        while (true) {

            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
            }
        }
    }
}