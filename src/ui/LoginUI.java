package ui;

import database.UserDatabase;
import model.*;

import java.util.Scanner;

public class LoginUI {

    private Scanner scanner;
    private UserDatabase userDatabase = new UserDatabase();

    public void start(Scanner scanner) {
    	this.scanner = scanner;
        String role = selectRole();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userDatabase.authenticate(role, username, password);

        if (user == null) {
            System.out.println("Login failed.");
            return;
        }

        System.out.println("Welcome " + user.getName());

        routeUser(user);
    }

    private String selectRole() {

        while (true) {

            System.out.println("\nSelect Role");
            System.out.println("1. Employee");
            System.out.println("2. Technician");
            System.out.println("3. Administrator");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": return "EMPLOYEE";
                case "2": return "TECHNICIAN";
                case "3": return "ADMINISTRATOR";
            }

            System.out.println("Invalid option.");
        }
    }

    private void routeUser(User user) {

        if (user instanceof Employee) {

            new EmployeeUI().start((Employee) user, scanner, userDatabase);

        } else if (user instanceof Technician) {

            new TechnicianUI().start((Technician) user, scanner, userDatabase);

        } else if (user instanceof Admin) {

            new AdminUI().start((Admin) user, scanner, userDatabase);
        }
    }
}