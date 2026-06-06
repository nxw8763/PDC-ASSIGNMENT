package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.AuditLog;
import model.enums.AuditAction;
import model.enums.AuditEntity;

public class AuditDAO {
	public static void addAuditLog(
	        int userId,
	        AuditAction action,
	        AuditEntity entity,
	        Integer entityId,
	        String details) {

	    String sql = """
	            INSERT INTO AUDIT_LOG
	            (
	    		    USER_ID,
			        ACTION_TYPE,
			        ENTITY_TYPE,
			        ENTITY_ID,
			        DETAILS
	            )
	            VALUES (?, ?, ?, ?, ?)
	            """;

	    try (
	            Connection conn =
	                    DatabaseConnection.getConnection();

	            PreparedStatement stmt =
	                    conn.prepareStatement(sql)
	    ) {

	        stmt.setInt(1, userId);
	        stmt.setString(2, action.name());
	        stmt.setString(3, entity.name());

	        if (entityId == null) {
	            stmt.setNull(4, Types.INTEGER);
	        } else {
	            stmt.setInt(4, entityId);
	        }

	        stmt.setString(5, details);

	        stmt.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static List<AuditLog> getAllAuditLogs() {

        List<AuditLog> logs = new ArrayList<>();

        String sql = """
                SELECT *
                FROM AUDIT_LOG
                ORDER BY TIMESTAMP DESC
                """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {
            	logs.add(new AuditLog(
            	        rs.getInt("AUDIT_ID"),
            	        rs.getInt("USER_ID"),
            	        AuditAction.valueOf(
            	                rs.getString("ACTION_TYPE")),
            	        AuditEntity.valueOf(
            	                rs.getString("ENTITY_TYPE")),
            	        (Integer) rs.getObject("ENTITY_ID"),
            	        rs.getString("DETAILS"),
            	        rs.getTimestamp("TIMESTAMP")
            	                .toLocalDateTime()
            	));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }
}
