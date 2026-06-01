package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import database.UserDatabase;
import model.Admin;
import model.Technician;
import model.Ticket;
import model.User;
import service.UserManagementService;
import service.CategoryService;
import service.TicketManagementService;

public class AdminUI {

    private Scanner scanner = new Scanner(System.in);
    private UserManagementService userService;
    private TicketManagementService ticketService;
    private CategoryService categoryService = new CategoryService();

    public void start(Admin admin, Scanner scanner, UserDatabase userdatabase) {
    	this.scanner = scanner;
    	this.userService =  new UserManagementService(userdatabase);
    	this.ticketService = new TicketManagementService(userdatabase);
        while (true) {

            System.out.println("\n=== Administrator Menu ===");
            System.out.println("1. View Users");
            System.out.println("2. Manage Users");
            System.out.println("3. Assign Role");
            System.out.println("4. Manage Categories");
            System.out.println("5. Generate Ticket Report");
            System.out.println("6. Assign Ticket");
            System.out.println("7. Logout");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    viewUsers();
                    break;

                case "2":
                    manageUsers();
                    break;

                case "3":
                    assignRole();
                    break;

                case "4":
                    manageCategories();
                    break;

                case "5":
                    generateReport();
                    break;

                case "6":
                    assignTicket();
                    break;

                case "7":
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
    
    private void viewUsers() {

        userService.getAllUsers()
                .forEach(user ->
                        System.out.println(user.getUserID() + " | "
                                + user.getName()));
    }
    
    private void displayUsers() {

        List<User> users = userService.getAllUsers();

        System.out.println("\n===== USERS =====");
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        for (User u : users) {
            System.out.println(
                u.getUserID() + " | " +
                u.getUsername() + " | " +
                u.getRole() + " | " +
                u.getName() + " | " +
                u.getEmail()
            );
        }
    }
    
    private void deleteUserUI() {

        displayUsers();

        System.out.print("\nEnter User ID to delete: ");
        int userID = Integer.parseInt(scanner.nextLine());

        boolean success = userService.removeUser(userID);

        if (success) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }
    
    private void editUserUI() {

        displayUsers();

        System.out.print("\nEnter User ID to edit: ");
        int userID = getSafeIntInput(scanner);

        System.out.println("\nWhat do you want to edit?");
        System.out.println("1. Name");
        System.out.println("2. Username");
        System.out.println("3. Password");
        System.out.println("4. Role");
        System.out.println("5. Email");

        String option = scanner.nextLine();
        String newValue;

        switch (option) {

            case "1":
                System.out.print("Enter new name: ");
                newValue = getSafeStringInput(scanner);
                userService.updateUserField(userID, "name", newValue);
                break;

            case "2":
                System.out.print("Enter new username: ");
                newValue = getSafeStringInput(scanner);
                userService.updateUserField(userID, "username", newValue);
                break;

            case "3":
                System.out.print("Enter new password: ");
                newValue = getSafeStringInput(scanner);
                userService.updateUserField(userID, "password", newValue);
                break;

            case "4":
                System.out.print("Enter new role (EMPLOYEE / TECHNICIAN / ADMINISTRATOR): ");
                newValue = getSafeStringInput(scanner);
                userService.updateUserField(userID, "role", newValue);
                break;

            case "5":
                System.out.print("Enter new email: ");
                newValue = getSafeStringInput(scanner);
                userService.updateUserField(userID, "email", newValue);
                break;

            default:
                System.out.println("Invalid option.");
        }

        System.out.println("User updated.");
    }
    
    private void manageUsers() {

        while (true) {

            System.out.println("\n===== MANAGE USERS =====");
            System.out.println("1. Create User");
            System.out.println("2. Delete User");
            System.out.println("3. Edit User");
            System.out.println("4. Back");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1" -> createUser();      // your existing system
                case "2" -> deleteUserUI();
                case "3" -> editUserUI();
                case "4" -> { return; }

                default -> System.out.println("Invalid option.");
            }
        }
    }
    
    private void createUser() {

        System.out.print("UserName: ");
        String username = getSafeStringInput(scanner);

        System.out.print("Password: ");
        String password = getSafeStringInput(scanner);

        System.out.println("Full name: ");
        String name = getSafeStringInput(scanner);
        
        String role = "";
        while(role.equals("")) {
        System.out.println("Role:");
        System.out.println("1 Employee");
        System.out.println("2 Technician");
        System.out.println("3 Administrator");

        String roleChoice = scanner.nextLine();

        switch(roleChoice) {
        case "1": role = "EMPLOYEE"; break;
        case "2": role = "TECHNICIAN"; break;
        case "3": role = "ADMINISTRATOR"; break;
        	default:
        }
        }
        System.out.println("Email: ");
        String email = getSafeStringInput(scanner);
        userService.createUser(name, username,password, role, email);

        System.out.println("User created.");
    }
    
    private void assignRole() {

        viewUsers();

        System.out.print("User ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.println("New Role:");
        System.out.println("1 Employee");
        System.out.println("2 Technician");
        System.out.println("3 Administrator");

        String roleChoice = scanner.nextLine();

        userService.updateUserRole(id, roleChoice);

        System.out.println("Role updated.");
    }
    
    private void manageCategories() {

        while (true) {

            System.out.println("\n=== Manage Categories ===");
            System.out.println("1. View Categories");
            System.out.println("2. Add Category");
            System.out.println("3. Delete Category");
            System.out.println("4. Back");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    viewCategories();
                    break;

                case "2":
                    addCategory();
                    break;

                case "3":
                    deleteCategory();
                    break;

                case "4":
                    return;
            }
        }
    }
    
    private void viewCategories() {

        List<String> categories = categoryService.getCategories();

        System.out.println("\n--- Categories ---");

        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
    }
    
    private void addCategory() {

        System.out.print("Enter new category: ");
        String category = getSafeStringInput(scanner);

        categoryService.addCategory(category);

        System.out.println("Category added.");
    }
    
    private void deleteCategory() {

        viewCategories();

        System.out.print("Enter category name to delete: ");
        String category = getSafeStringInput(scanner);

        categoryService.deleteCategory(category);

        System.out.println("Category deleted.");
    }
    
    private void assignTicket() {

        List<Ticket> tickets = ticketService.getAllTickets();

        System.out.println("\n--- Tickets ---");
        if (tickets.isEmpty()) {
            System.out.println("No tickets available.");
            return; // automatically backs out
        }
        for (int i = 0; i < tickets.size(); i++) {

            Ticket t = tickets.get(i);

            System.out.println((i + 1) + ". " +
                    t.getTicketID() + " | " +
                    t.getTitle() + " | " +
                    t.getStatus());
        }
        int ticketChoice;
        while(true) {
        	System.out.print("Select ticket: ");
        	ticketChoice = Integer.parseInt(scanner.nextLine());
            if (ticketChoice >= 1 && ticketChoice <= tickets.size()) {
                break;
            }

            System.out.println("Invalid option.");
        }
        Ticket selectedTicket = tickets.get(ticketChoice - 1);

        // NOW TECHNICIAN SELECTION

        List<User> users = userService.getAllUsers();

        List<Technician> techs = new ArrayList<>();

        System.out.println("\n--- Technicians ---");
        for (User u : users) {

            if (u instanceof Technician) {
                techs.add((Technician) u);
            }
        }

        for (int i = 0; i < techs.size(); i++) {

            System.out.println((i + 1) + ". " +
                    techs.get(i).getName());
        }
        if (techs.isEmpty()) {
        	System.out.println("No users found.");
        	return;
        }
        int techChoice;
        while(true) {
        	System.out.print("Select technician: ");
        	techChoice = getSafeIntInput(scanner);
        	 if (techChoice >= 1 && techChoice <= techs.size()) {
        	        break;
        	    }

        	    System.out.println("Invalid option.");
        }
        ticketService.assignTicketByAdmin(
                selectedTicket.getTicketID(),
                techChoice - 1
        );
    }
    
    private void generateReport() {
        ticketService.generateReport();
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