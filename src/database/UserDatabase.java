package database;

import model.*;
import java.io.*;
import java.util.*;

public class UserDatabase {

    private static final String FILE = "data/users.txt";
    
    private static class UserRecord {
        int id;
        String username;
        String password;
        String role;
        String name;
        String email;
    }
    
    private List<UserRecord> loadRecords() {

        List<UserRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");

                UserRecord r = new UserRecord();

                r.id = Integer.parseInt(parts[0]);
                r.username = parts[1];
                r.password = parts[2];
                r.role = parts[3];
                r.name = parts[4];
                r.email = parts[5];

                records.add(r);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    public List<User> fetchUsers() {

        List<UserRecord> records = loadRecords();
        List<User> users = new ArrayList<>();

        for (UserRecord r : records) {

            switch (r.role) {

                case "EMPLOYEE":
                    users.add(new Employee(r.id, r.username, r.name, r.email));
                    break;

                case "TECHNICIAN":
                    users.add(new Technician(r.id, r.username, r.name, r.email));
                    break;

                case "ADMINISTRATOR":
                    users.add(new Admin(r.id, r.username, r.name, r.email));
                    break;
            }
        }

        return users;
    }
    public void saveUser(int id, String name, String username,String password, String role, String email) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {

        	writer.newLine();
            writer.write(id + "|" + username + "|" + password + "|" + role + "|" + name + "|" + email);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User updated) {

        List<UserRecord> records = loadRecords();

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE))) {

            for (UserRecord r : records) {

                if (r.id == updated.getUserID()) {

                    writer.println(
                            updated.getUserID()+ "|" +
                            updated.getUsername() + "|" +
                            r.password + "|" +   // PRESERVE PASSWORD
                            updated.getRole()+ "|" +
                            updated.getName() + "|" +
                            updated.getEmail()
                    );

                } else {

                    writer.println(
                            r.id + "|" +
                            r.username + "|" +
                            r.password + "|" +
                            r.role + "|" +
                            r.name + "|" +
                            r.email
                    );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public User authenticate(String role, String username, String password) {

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");

                String fileUsername = parts[1];
                String filePassword = parts[2];
                String fileRole = parts[3];

                if (fileUsername.equals(username)
                        && filePassword.equals(password) && fileRole.equals(role)) {

                    int id = Integer.parseInt(parts[0]);
                    String name = parts[4];
                    String email = parts[5];

                    // rebuild correct polymorphic object
                    return createUserFromRole(id, fileRole, fileUsername,name, email);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public boolean removeUser(int userID) {

        List<String> remainingUsers = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");

                int id = Integer.parseInt(parts[0]);

                if (id == userID) {
                    found = true;
                    continue; // skip this user
                }

                remainingUsers.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!found) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {

            for (String user : remainingUsers) {
                writer.write(user);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    
    public void updateUserField(int userID, String field, String newValue) {

        List<String> updatedUsers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");

                int id = Integer.parseInt(parts[0]);

                if (id == userID) {

                    switch (field) {

                        case "username" -> parts[1] = newValue;
                        case "password" -> parts[2] = newValue;
                        case "role" -> parts[3] = newValue;
                        case "name" -> parts[4] = newValue;
                        case "email" -> parts[5] = newValue;

                    }

                    line = String.join("|", parts);
                }

                updatedUsers.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {

            for (String user : updatedUsers) {
                writer.write(user);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private User createUserFromRole(int id,
            String role,
            String username,
            String name,
            String email) {
	
		switch (role) {
		
		case "EMPLOYEE":
		return new Employee(id, username, name, email);
		
		case "TECHNICIAN":
		return new Technician(id, username, name, email);
		
		case "ADMINISTRATOR":
		return new Admin(id, username, name, email);
		
		default:
		return null;
	}
}
    
    
}