package service;

import dao.UserDAO;
import model.*;

import java.util.List;

public class UserManagementService {

    private final UserDAO userDAO;

    public UserManagementService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers() {
        return userDAO.fetchUsers();
    }

    public void createUser(String name, String username,
                           String password, String role,
                           String email) {

        userDAO.saveUser(name, username, password, role, email);
    }

    public boolean removeUser(int userID) {

        if (userID <= 0) return false;

        userDAO.deleteUser(userID);
        return true;
    }

    public void updateUserField(int userID, String field, String value) {

        User user = userDAO.getUserByID(userID);
        if (user == null) return;

        switch (field) {

            case "username" -> user.setUsername(value);
            case "email" -> user.setEmail(value);
            case "name" -> user.setName(value);
            case "role" -> user.setRole(value);
        }

        userDAO.updateUser(user);
    }

    public void updateUserRole(int userID, String role) {

        User user = userDAO.getUserByID(userID);
        if (user == null) return;

        User updated = switch (role) {

            case "EMPLOYEE" -> new Employee(userID, user.getUsername(), user.getName(), user.getEmail());
            case "TECHNICIAN" -> new Technician(userID, user.getUsername(), user.getName(), user.getEmail());
            case "ADMINISTRATOR" -> new Admin(userID, user.getUsername(), user.getName(), user.getEmail());
            default -> null;
        };

        if (updated != null) {
            userDAO.updateUser(updated);
        }
    }
    
    public User authenticate(String username, String password) {

        if (username == null || username.isBlank()) {
            return null;
        }

        if (password == null || password.isBlank()) {
            return null;
        }

        return userDAO.authenticate(username, password);
    }
}