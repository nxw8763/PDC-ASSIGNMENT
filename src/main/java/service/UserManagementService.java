package service;

import dao.UserDAO;
import model.*;
import model.enums.AuditAction;
import model.enums.AuditEntity;

import java.util.List;

public class UserManagementService {

    private final UserDAO userDAO;

    public UserManagementService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers(Admin admin) {
    	requireAdmin(admin);
    	List<User> users = userDAO.fetchUsers();
    	AuditService.addAuditLog(admin.getUserID(), AuditAction.FETCH, AuditEntity.USER , users.size(), admin.getUsername() +  " fetched " + users.size() + " users");
        return users;
    }
    
    public List<Technician> getAllTechnicians(Admin admin){
    	requireAdmin(admin);
    	List<Technician> techs = userDAO.getTechnicians();
    	AuditService.addAuditLog(admin.getUserID(), AuditAction.FETCH, AuditEntity.USER , techs.size(), admin.getUsername() +  " fetched " + techs.size());
    	return techs;
    }

    public void createUser(Admin admin, String name, String username,
                           String password, String role,
                           String email) {

    	requireAdmin(admin);
    	validateUser(name, username, password, role, email);
    	AuditService.addAuditLog(admin.getUserID(), AuditAction.FETCH, AuditEntity.USER , 0, admin.getUsername() +  " created new user:" + username + " | " + email + " | " + role);
    	if (userDAO.usernameExists(username)) {
    	    throw new IllegalArgumentException(
    	        "Username already exists."
    	    );
    	}
    	userDAO.saveUser(name, username, password, role, email);
    }

    public boolean removeUser(Admin admin, int userID) {

    	requireAdmin(admin);
    	AuditService.addAuditLog(admin.getUserID(), AuditAction.DELETE, AuditEntity.USER , userID, admin.getUsername() +  " removed user#" + userID);
        if (userID <= 0) return false;
        userDAO.deleteUser(userID);
        return true;
    }

    public void updateUserField(Admin admin, int userID, String field, String value) {
    	requireAdmin(admin);
        User user = userDAO.getUserByID(userID);
        if (user == null) return;
        AuditService.addAuditLog(admin.getUserID(), AuditAction.UPDATE, AuditEntity.USER , userID, admin.getUsername() +  " updated " + user.getUsername() + field + " to " + value);
        switch(field) {

        case "username" -> {

            if(value == null || value.isBlank()) {
                throw new IllegalArgumentException(
                        "Username cannot be blank."
                );
            }

            if(userDAO.usernameExists(value)
                    && !value.equals(user.getUsername())) {

                throw new IllegalArgumentException(
                        "Username already exists."
                );
            }

            user.setUsername(value);
        }

        case "email" -> {

            if(value == null || value.isBlank()) {
                throw new IllegalArgumentException(
                        "Email cannot be blank."
                );
            }

            if(!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                throw new IllegalArgumentException(
                        "Invalid email."
                );
            }

            user.setEmail(value);
        }
    }
        
        userDAO.updateUser(user);
    }
    
    public void updateUserPassword(Admin admin, int userID, String value) {
    	requireAdmin(admin);
        User user = userDAO.getUserByID(userID);
        if (user == null) return;
        AuditService.addAuditLog(admin.getUserID(), AuditAction.UPDATE, AuditEntity.USER , userID, admin.getUsername() +  " updated " + user.getUsername() + " password");
        userDAO.updateUserPassword(userID, value);
    }
    
    public User authenticate(String username, String password) {

        if (username == null || username.isBlank()) {
            return null;
        }

        if (password == null || password.isBlank()) {
            return null;
        }
        User user = userDAO.authenticate(username, password); 
        if (user == null) return null; 
        AuditService.addAuditLog(user.getUserID(), AuditAction.LOGIN, AuditEntity.USER , user.getUserID(), user.getUsername() +  " loged in");
        return user;
    }
    
    private void requireAdmin(User currentUser) {

        if (!(currentUser instanceof Admin)) {

        	AuditService.addAuditLog(currentUser.getUserID(), AuditAction.VIOLATION, AuditEntity.USER , currentUser.getUserID(), currentUser.getUsername() +  " violated access policy for users");
            throw new SecurityException(
                    "Only administrators may manage users.");
        }
    }
    
    private void validateUser(
            String name,
            String username,
            String password,
            String role,
            String email) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role is required.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}