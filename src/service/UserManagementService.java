package service;

import database.UserDatabase;
import model.*;

import java.util.List;

public class UserManagementService {

    private UserDatabase database;
    
    public UserManagementService(UserDatabase userdatabase) {
    	this.database = userdatabase;
    }

    public List<User> getAllUsers() {
        return database.fetchUsers();
    }

    public void createUser(String name, String username,String password, String roleChoice, String email) {

        int newID = generateNextID();

        database.saveUser(newID ,name, username, password, roleChoice, email);
    }
    
    public boolean removeUser(int userID) {

        if (userID <= 0) {
            System.out.println("Invalid user ID.");
            return false;
        }

        boolean removed = database.removeUser(userID);

        if (!removed) {
            System.out.println("User not found.");
            return false;
        }

        return true;
    }
    
    public void updateUserField(int userID, String field, String newValue) {
        database.updateUserField(userID, field, newValue);
    }

    public void updateUserRole(int userID, String roleChoice) {

        List<User> users = database.fetchUsers();

        for (User user : users) {

            if (user.getUserID() == userID) {

                User newUser;

                switch (roleChoice) {

                    case "1":
                        newUser = new Employee(
                                user.getUserID(),
                                user.getUsername(),
                                user.getName(),
                                user.getEmail()
                        );
                        break;

                    case "2":
                        newUser = new Technician(
                                user.getUserID(),
                                user.getUsername(),
                                user.getName(),
                                user.getEmail()
                        );
                        break;

                    case "3":
                        newUser = new Admin(
                                user.getUserID(),
                                user.getUsername(),
                                user.getName(),
                                user.getEmail()
                        );
                        break;

                    default:
                        System.out.println("Invalid role");
                        return;
                }

                database.updateUser(newUser);

                return;
            }
        }

        System.out.println("User not found.");
    }

    private int generateNextID() {

        List<User> users = database.fetchUsers();

        int max = 0;

        for (User user : users) {

            if (user.getUserID() > max) {
                max = user.getUserID();
            }
        }

        return max + 1;
    }
}