package dao;

import database.DatabaseConnection;
import model.*;
import model.users.Admin;
import model.users.Employee;
import model.users.Technician;
import model.users.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> fetchUsers() {

        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM USERS";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                users.add(mapUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User getUserByID(int userID) {

        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Technician> getTechnicians() {

        List<Technician> techs = new ArrayList<>();

        String sql = "SELECT * FROM USERS WHERE ROLE = 'TECHNICIAN'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                techs.add(new Technician(
                        rs.getInt("USER_ID"),
                        rs.getString("USERNAME"),
                        rs.getString("NAME"),
                        rs.getString("EMAIL")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return techs;
    }

    public void saveUser(String name, String username, String password,
                         String role, String email) {

        String sql = """
            INSERT INTO USERS (USERNAME, PASSWORD, ROLE, NAME, EMAIL)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.setString(4, name);
            stmt.setString(5, email);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {

        String sql = """
            UPDATE USERS
            SET USERNAME = ?, ROLE = ?, NAME = ?, EMAIL = ?
            WHERE USER_ID = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getRole());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getEmail());
            stmt.setInt(5, user.getUserID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateUserPassword(int userID, String password) {

        String sql = """
            UPDATE USERS
            SET PASSWORD = ?
            WHERE USER_ID = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, password);
            stmt.setInt(2, userID);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userID) {

        String sql = "DELETE FROM USERS WHERE USER_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User authenticate(String username, String password) {

        String sql =
                "SELECT user_id, username, role, name, email " +
                "FROM Users " +
                "WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            int id = rs.getInt("user_id");
            String role = rs.getString("role");
            String name = rs.getString("name");
            String email = rs.getString("email");

            return createUser(id, username, role, name, email);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // factory method
    private User createUser(int id,
                            String username,
                            String role,
                            String name,
                            String email) {

        return switch (role) {

            case "EMPLOYEE" ->
                    new Employee(id, username, name, email);

            case "TECHNICIAN" ->
                    new Technician(id, username, name, email);

            case "ADMINISTRATOR" ->
                    new Admin(id, username, name, email);

            default -> null;
        };
    }

    private User mapUser(ResultSet rs) throws SQLException {

        int id = rs.getInt("USER_ID");
        String role = rs.getString("ROLE");

        return switch (role) {

            case "EMPLOYEE" -> new Employee(
                    id,
                    rs.getString("USERNAME"),
                    rs.getString("NAME"),
                    rs.getString("EMAIL")
            );

            case "TECHNICIAN" -> new Technician(
                    id,
                    rs.getString("USERNAME"),
                    rs.getString("NAME"),
                    rs.getString("EMAIL")
            );

            case "ADMINISTRATOR" -> new Admin(
                    id,
                    rs.getString("USERNAME"),
                    rs.getString("NAME"),
                    rs.getString("EMAIL")
            );

            default -> null;
        };
    }
    
    // validation
    public boolean usernameExists(String username) {

        String sql =
                "SELECT 1 FROM USERS WHERE USERNAME = ?";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}