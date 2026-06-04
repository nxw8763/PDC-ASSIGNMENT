package service;

import dao.UserDAO;
import model.*;

import java.util.List;

public class UserManagementService {

    private final UserDAO userDAO;

    public UserManagementService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers(Admin admin) {
    	requireAdmin(admin);
        return userDAO.fetchUsers();
    }

    public void createUser(Admin admin, String name, String username,
                           String password, String role,
                           String email) {

    	requireAdmin(admin);
        userDAO.saveUser(name, username, password, role, email);
    }

    public boolean removeUser(Admin admin, int userID) {

    	requireAdmin(admin);
        if (userID <= 0) return false;

        userDAO.deleteUser(userID);
        return true;
    }

    public void updateUserField(Admin admin, int userID, String field, String value) {
    	requireAdmin(admin);
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

    public void updateUserRole(Admin admin, int userID, String role) {
    	requireAdmin(admin);
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
    
    private void requireAdmin(User currentUser) {

        if (!(currentUser instanceof Admin)) {

            throw new SecurityException(
                    "Only administrators may manage users.");
        }
    }
}