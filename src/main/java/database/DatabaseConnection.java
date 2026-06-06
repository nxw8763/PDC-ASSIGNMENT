package database;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:derby:ServiceDeskDB;create=true";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {

        if(connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }

        return connection;
    }
}