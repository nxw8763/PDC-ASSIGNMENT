package ui;

import model.*;
import service.CategoryService;
import service.TicketManagementService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import database.UserDatabase;

public class EmployeeUI {

    private TicketManagementService ticketService;
    private CategoryService categoryService = new CategoryService();
    private Scanner scanner;

    public void start(Employee emp, Scanner scanner, UserDatabase userdatabase) {
    	this.scanner = scanner;
    	this.ticketService  = new TicketManagementService(userdatabase);
        while (true) {

            System.out.println("\nEmployee Menu");
            System.out.println("1. Create Ticket");
            System.out.println("2. View My Open Tickets");
            System.out.println("3. Comment on ticket");
            System.out.println("4. Logout");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                	createTicket(emp);
                    break;

                case "2":
                	viewTickets(emp);
                    break;
                case "3":
                	comment(emp);
                	break;
                case "4":
                	return;
            }
        }
    }
    
    private void createTicket(Employee emp) {
        System.out.print("Title: ");
        String title = getSafeStringInput(scanner);

        System.out.print("Description: ");
        String desc = getSafeStringInput(scanner);

        String category = selectCategory();

        Priority priority = selectPriority();

        Ticket ticket = ticketService.createTicket(
                title,
                desc,
                category,
                priority,
                emp
        );

        System.out.println("Ticket created with ID: " + ticket.getTicketID());
    }
    
    public void viewTickets(Employee emp) {
    	  List<Ticket> tickets =
                  ticketService.getOpenTicketsByUser(emp.getUserID());

          for (Ticket t : tickets) {
              t.printTicket();
          }
    }
    
    private void comment(Employee emp) {
    	List<Ticket> ticketsComments = ticketService.getOpenTicketsByUser(emp.getUserID());

    	for (int i = 0; i < ticketsComments.size(); i++) {

    	    Ticket t = ticketsComments.get(i);

    	    System.out.println((i + 1) + ". " +
    	            t.getTicketID() + " | " +
    	            t.getTitle() + " | " +
    	            t.getCategory());
    	}
    	int ticketChoice;
    	while(true) {
    		System.out.print("Select ticket number: ");
    		ticketChoice = getSafeIntInput(scanner);
    		 if (ticketChoice >= 1 && ticketChoice <= ticketsComments.size()) {
    		        break;
    		    }

    		    System.out.println("Invalid option.");
    	}
    	Ticket selected = ticketsComments.get(ticketChoice - 1);
    	System.out.print("Comment Title: ");
    	String title = getSafeStringInput(scanner);

    	System.out.print("Comment Description: ");
    	String desc = getSafeStringInput(scanner);
    	
    	Comment comment = new Comment(
    	        title,
    	        desc,
    	        emp.getEmail(),
    	        LocalDateTime.now()
    	);

    	ticketService.commentTicket(selected, comment);
        return;
    }
    
    private String selectCategory() {

        List<String> categories = categoryService.getCategories();

        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
        int choice;
        while(true) {
	        System.out.print("Select category: ");
	        choice = getSafeIntInput(scanner);
	        if (choice >= 1 && choice <= categories.size()) {
	            break;
	        }

	        System.out.println("Invalid option.");
        }
        return categories.get(choice - 1);
    }
    
    private Priority selectPriority() {

        while(true) {

            System.out.println("Select Priority");
            System.out.println("1. Low");
            System.out.println("2. Medium");
            System.out.println("3. High");
            System.out.println("4. Critical");

            String choice = scanner.nextLine();

            switch(choice) {

                case "1":
                    return Priority.LOW;

                case "2":
                    return Priority.MEDIUM;

                case "3":
                    return Priority.HIGH;

                case "4":
                    return Priority.CRITICAL;

                default:
                    System.out.println("Invalid option");
            }
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
}