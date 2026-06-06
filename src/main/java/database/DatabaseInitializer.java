package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

	public static void initialize() {
		//resetDatabase();
	    try (
	            Connection conn = DatabaseConnection.getConnection();
	            Statement stmt = conn.createStatement()
	    ) {

	        createUsersTable(stmt);
	        createTicketsTable(stmt);
	        createCommentsTable(stmt);
	        createCategoriesTable(stmt);
	        createAuditLogTable(stmt);
	        
	        seedData(conn);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	//debug tool
	public static void resetDatabase() {
	    try (
	            Connection conn = DatabaseConnection.getConnection();
	            Statement stmt = conn.createStatement()
	    ) {
	    	stmt.executeUpdate("DROP TABLE AUDIT_LOG");
	        stmt.executeUpdate("DROP TABLE COMMENTS");
	        stmt.executeUpdate("DROP TABLE TICKETS");
	        stmt.executeUpdate("DROP TABLE USERS");
	        stmt.executeUpdate("DROP TABLE CATEGORIES");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	private static void seedData(Connection conn) {

	    try (Statement stmt = conn.createStatement()) {

	        // -------------------
	        // USERS
	        // -------------------
	    	ResultSet userTable = stmt.executeQuery("SELECT COUNT(*) FROM USERS");
	    	userTable.next();
	    	if(userTable.getInt(1) == 0) {
	    		stmt.executeUpdate("""
		            INSERT INTO USERS (USERNAME, PASSWORD, ROLE, NAME, EMAIL)
		            VALUES
		            ('admin', 'admin123', 'ADMINISTRATOR', 'System Admin', 'admin@sys.com')
		        """);
	
		        stmt.executeUpdate("""
		            INSERT INTO USERS (USERNAME, PASSWORD, ROLE, NAME, EMAIL)
		            VALUES
		            ('tech1', '1234', 'TECHNICIAN', 'John Tech', 'tech1@sys.com')
		        """);
	
		        stmt.executeUpdate("""
		            INSERT INTO USERS (USERNAME, PASSWORD, ROLE, NAME, EMAIL)
		            VALUES
		            ('emp1', '1234', 'EMPLOYEE', 'Jane Employee', 'emp1@sys.com')
		        """);
		        System.out.println("user data inserted.");
	    	}
	        // -------------------
	        // CATEGORIES
	        // -------------------
	    	ResultSet categoryTable = stmt.executeQuery("SELECT COUNT(*) FROM CATEGORIES");
	    	categoryTable.next();
	    	if(categoryTable.getInt(1) == 0) {
		        stmt.executeUpdate("""
		            INSERT INTO CATEGORIES (CATEGORY_NAME)
		            VALUES ('Hardware')
		        """);
	
		        stmt.executeUpdate("""
		            INSERT INTO CATEGORIES (CATEGORY_NAME)
		            VALUES ('Software')
		        """);
	
		        stmt.executeUpdate("""
		            INSERT INTO CATEGORIES (CATEGORY_NAME)
		            VALUES ('Network')
		        """);
	        	System.out.println("category data inserted.");
	    	}
	    } catch (SQLException e) {
	        System.out.println("Seed data already exists or failed:");
	        e.printStackTrace();
	    }
	}

    private static void createUsersTable(Statement stmt) {

        try {

            stmt.executeUpdate(
                    """
                    CREATE TABLE USERS (
                        USER_ID INT GENERATED ALWAYS AS IDENTITY
                            PRIMARY KEY,
                            
                        USERNAME VARCHAR(50) NOT NULL UNIQUE,
                        
                        PASSWORD VARCHAR(255) NOT NULL,
                        
                        ROLE VARCHAR(20) NOT NULL,
                        
                        NAME VARCHAR(100) NOT NULL,
                        
                        EMAIL VARCHAR(100) NOT NULL
                    )
                    """
            );

            System.out.println("USERS table created.");

        } catch (SQLException ignored) {
        }
    }

    private static void createTicketsTable(Statement stmt) {

        try {

            stmt.executeUpdate(
                    """
                    CREATE TABLE TICKETS (
                    
                        TICKET_ID INT GENERATED ALWAYS AS IDENTITY
                            PRIMARY KEY,
                            
                        TITLE VARCHAR(100) NOT NULL,
                        
                        DESCRIPTION VARCHAR(4000),
                        
                        CATEGORY VARCHAR(100),
                        
                        PRIORITY VARCHAR(20),
                        
                        STATUS VARCHAR(20),
                        
                        ASSIGNED_TECHNICIAN_ID INT,
                        
                        CREATED_BY_USER_ID INT NOT NULL,
                        
                        CREATED_DATE TIMESTAMP,
                        
                        FOREIGN KEY (CREATED_BY_USER_ID)
                            REFERENCES USERS(USER_ID)
                    )
                    """
            );

            System.out.println("TICKETS table created.");

        } catch (SQLException ignored) {
        }
    }

    private static void createCommentsTable(Statement stmt) {

        try {

            stmt.executeUpdate(
                    """
                    CREATE TABLE COMMENTS (
                    
                        COMMENT_ID INT GENERATED ALWAYS AS IDENTITY
                            PRIMARY KEY,
                            
                        TICKET_ID INT NOT NULL,
                        
                        TITLE VARCHAR(100),
                        
                        DESCRIPTION VARCHAR(4000),
                        
                        CREATED_BY VARCHAR(50),
                        
                        CREATED_DATE TIMESTAMP,
                        
                        FOREIGN KEY (TICKET_ID)
                            REFERENCES TICKETS(TICKET_ID)
                    )
                    """
            );

            System.out.println("COMMENTS table created.");

        } catch (SQLException ignored) {
        }
    }

    private static void createCategoriesTable(Statement stmt) {

        try {

            stmt.executeUpdate(
                    """
                    CREATE TABLE CATEGORIES (
                        CATEGORY_NAME VARCHAR(100)
                            PRIMARY KEY
                    )
                    """
            );

            System.out.println("CATEGORIES table created.");

        } catch (SQLException ignored) {
        }
    }
    
    private static void createAuditLogTable(Statement stmt) {

        try {

            stmt.executeUpdate(
                    """
                    CREATE TABLE AUDIT_LOG (
                    
                        AUDIT_ID INT GENERATED ALWAYS AS IDENTITY
                            PRIMARY KEY,
                            
                        USER_ID INT NOT NULL,
                        
                        ACTION_TYPE VARCHAR(50) NOT NULL,
                        
                        ENTITY_TYPE VARCHAR(50) NOT NULL,
                        
                        ENTITY_ID INT,
                        
                        DETAILS VARCHAR(500),
                        
                        TIMESTAMP TIMESTAMP
                            DEFAULT CURRENT_TIMESTAMP
                            NOT NULL,
                        
                        FOREIGN KEY (USER_ID)
                            REFERENCES USERS(USER_ID)
                    )
                    """
            );

            System.out.println("AUDIT_LOG table created.");

        } catch (SQLException ignored) {
        }
    }
}