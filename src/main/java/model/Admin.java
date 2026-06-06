package model;

public class Admin extends User {
    public Admin(int userID, String username,String name, String email) {
        super(userID, username,name, email, "ADMINISTRATOR");
    }

}