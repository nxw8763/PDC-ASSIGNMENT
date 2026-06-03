package dao;

import database.DatabaseConnection;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public List<Ticket> fetchTickets() {

        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM TICKETS";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("TICKET_ID");

                Ticket t = new Ticket(
                        id,
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("CATEGORY"),
                        Priority.valueOf(rs.getString("PRIORITY")),
                        rs.getInt("CREATED_BY_USER_ID"),
                        Status.valueOf(rs.getString("STATUS")),
                        rs.getTimestamp("CREATED_DATE").toLocalDateTime()
                );

                int techId = rs.getInt("ASSIGNED_TECHNICIAN_ID");
                t.setAssignedTechnicianID(techId);

                t.setComments(fetchComments(conn, id));

                tickets.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public Ticket getTicketByID(int ticketID) {

        String sql = "SELECT * FROM TICKETS WHERE TICKET_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketID);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Ticket t = new Ticket(
                        ticketID,
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("CATEGORY"),
                        Priority.valueOf(rs.getString("PRIORITY")),
                        rs.getInt("CREATED_BY_USER_ID"),
                        Status.valueOf(rs.getString("STATUS")),
                        rs.getTimestamp("CREATED_DATE").toLocalDateTime()
                );

                t.setAssignedTechnicianID(rs.getInt("ASSIGNED_TECHNICIAN_ID"));
                t.setComments(fetchComments(conn, ticketID));

                return t;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int saveTicket(Ticket t) {

        String sql = """
            INSERT INTO TICKETS
            (TITLE, DESCRIPTION, CATEGORY, PRIORITY, STATUS,
             ASSIGNED_TECHNICIAN_ID, CREATED_BY_USER_ID, CREATED_DATE)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, t.getTitle());
            stmt.setString(2, t.getDescription());
            stmt.setString(3, t.getCategory());
            stmt.setString(4, t.getPriority().name());
            stmt.setString(5, t.getStatus().name());
            stmt.setInt(6, t.getAssignedTechnicianID());
            stmt.setInt(7, t.getCreatedByUserID());
            stmt.setTimestamp(8, Timestamp.valueOf(t.getCreatedDate()));

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();

            if (keys.next()) {
                int id = keys.getInt(1);

                for (Comment c : t.getComments()) {
                    saveComment(conn, id, c);
                }

                return id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void updateTicket(Ticket t) {

        String sql = """
            UPDATE TICKETS
            SET TITLE=?, DESCRIPTION=?, CATEGORY=?, PRIORITY=?,
                STATUS=?, ASSIGNED_TECHNICIAN_ID=?
            WHERE TICKET_ID=?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getTitle());
            stmt.setString(2, t.getDescription());
            stmt.setString(3, t.getCategory());
            stmt.setString(4, t.getPriority().name());
            stmt.setString(5, t.getStatus().name());
            stmt.setInt(6, t.getAssignedTechnicianID());
            stmt.setInt(7, t.getTicketID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addComment(int ticketID, Comment c) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            saveComment(conn, ticketID, c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Comment> fetchComments(Connection conn, int ticketID) throws SQLException {

        List<Comment> comments = new ArrayList<>();

        String sql = "SELECT * FROM COMMENTS WHERE TICKET_ID=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                comments.add(new Comment(
                        rs.getString("TITLE"),
                        rs.getString("DESCRIPTION"),
                        rs.getString("CREATED_BY"),
                        rs.getTimestamp("CREATED_DATE").toLocalDateTime()
                ));
            }
        }

        return comments;
    }

    private void saveComment(Connection conn, int ticketID, Comment c) throws SQLException {

        String sql = """
            INSERT INTO COMMENTS
            (TICKET_ID, TITLE, DESCRIPTION, CREATED_BY, CREATED_DATE)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketID);
            stmt.setString(2, c.getTitle());
            stmt.setString(3, c.getDescription());
            stmt.setString(4, c.getCreatedByUser());
            stmt.setTimestamp(5, Timestamp.valueOf(c.getCreatedDate()));

            stmt.executeUpdate();
        }
    }
}