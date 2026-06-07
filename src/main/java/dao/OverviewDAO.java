package dao;

import database.DatabaseConnection;
import model.LabelCount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OverviewDAO {

	public List<LabelCount> getTicketTrend() {

	    List<LabelCount> data = new ArrayList<>();

	    String sql = """
	        SELECT
	            HOUR(CREATED_DATE) AS HOUR_OF_DAY,
	            COUNT(*) AS TOTAL
	        FROM TICKETS
	        WHERE DATE(CREATED_DATE) = CURRENT_DATE
	        GROUP BY HOUR(CREATED_DATE)
	        ORDER BY HOUR_OF_DAY
	        """;

	    try (
	            Connection conn = DatabaseConnection.getConnection();
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            ResultSet rs = stmt.executeQuery()
	    ) {

	        while (rs.next()) {

	            int hour = rs.getInt("HOUR_OF_DAY");

	            data.add(
	                    new LabelCount(
	                            String.format("%02d:00", hour),
	                            rs.getInt("TOTAL")
	                    )
	            );
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return data;
	}

    public List<LabelCount> getPriorityCounts() {
    	
        String sql = """
            SELECT PRIORITY,
                   COUNT(*) AS TOTAL
            FROM TICKETS
            GROUP BY PRIORITY
            """;

        return getLabelCounts(sql, "PRIORITY");
    }

    public List<LabelCount> getStatusCounts() {

        String sql = """
            SELECT STATUS,
                   COUNT(*) AS TOTAL
            FROM TICKETS
            WHERE STATUS <> 'CLOSED'
            GROUP BY STATUS
            """;

        return getLabelCounts(sql, "STATUS");
    }

    public List<LabelCount> getCategoryCounts() {

        String sql = """
            SELECT CATEGORY,
                   COUNT(*) AS TOTAL
            FROM TICKETS
            GROUP BY CATEGORY
            ORDER BY TOTAL DESC
            """;

        return getLabelCounts(sql, "CATEGORY");
    }

    public List<LabelCount> getTechnicianWorkload() {

        String sql = """
            SELECT
                COALESCE(u.NAME, 'Unassigned') AS TECHNICIAN,
                COUNT(*) AS TOTAL
            FROM TICKETS t
            LEFT JOIN USERS u
                ON t.ASSIGNED_TECHNICIAN_ID = u.USER_ID
            WHERE t.STATUS <> 'CLOSED'
            GROUP BY COALESCE(u.NAME, 'Unassigned')
            ORDER BY TOTAL DESC
            """;

        return getLabelCounts(sql, "TECHNICIAN");
    }

    private List<LabelCount> getLabelCounts(
            String sql,
            String column) {

        List<LabelCount> data = new ArrayList<>();

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                data.add(
                        new LabelCount(
                                rs.getString(column),
                                rs.getInt("TOTAL")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}