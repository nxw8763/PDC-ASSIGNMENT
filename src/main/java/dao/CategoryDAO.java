package dao;

import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<String> getCategories() {

        List<String> categories = new ArrayList<>();

        String sql = "SELECT CATEGORY_NAME FROM CATEGORIES";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public void addCategory(String category) {

        String sql = "INSERT INTO CATEGORIES (CATEGORY_NAME) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(String category) {

        String sql = "DELETE FROM CATEGORIES WHERE CATEGORY_NAME = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean categoryExists(String category) {

        String sql =
                "SELECT 1 FROM CATEGORIES WHERE CATEGORY_NAME = ?";

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, category);

            return stmt.executeQuery().next();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}