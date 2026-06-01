package model;

public class User {

    protected int userID;
    protected String name;
    protected String email;
    protected String username;
    protected String role;

    public User(int userID, String username,String name, String email, String role) {
        this.userID = userID;
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }
    
    public String getUsername() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    
    public String getRole() {
    	return role;
    }
}