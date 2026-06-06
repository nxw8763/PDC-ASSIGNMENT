package model;

public class Employee extends User {
    public Employee(int userID, String username,String name, String email) {
        super(userID, username,name, email, "EMPLOYEE");
    }

}