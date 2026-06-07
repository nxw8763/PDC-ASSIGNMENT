package model.users;

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

    public void setUserID(int userID) {
    	this.userID = userID;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public void setRole(String role) {
    	this.role = role;
    }
    
    public int getUserID() {
        return userID;
    }
    
    public String getUsername() {
        return username;
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