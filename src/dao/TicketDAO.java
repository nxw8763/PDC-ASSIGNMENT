package dao;

import database.DatabaseConnection;
import model.*;
import model.enums.Priority;
import model.enums.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public List<Ticket> fetchAllTickets() {

        List<Ticket> tickets = new ArrayList<>();

        String sql = """
                SELECT t.*, u.EMAIL AS TECH_EMAIL
                FROM TICKETS t
                LEFT JOIN USERS u
                    ON t.ASSIGNED_TECHNICIAN_ID = u.USER_ID
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tickets.add(mapTicket(conn, rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public List<Ticket> fetchOpenOrAssignedTickets(int technicianID) {

        List<Ticket> tickets = new ArrayList<>();

        String sql = """
                SELECT t.*, u.EMAIL AS TECH_EMAIL
                FROM TICKETS t
                LEFT JOIN USERS u
                    ON t.ASSIGNED_TECHNICIAN_ID = u.USER_ID
                WHERE t.STATUS = ?
                   OR t.ASSIGNED_TECHNICIAN_ID = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, Status.OPEN.name());
            stmt.setInt(2, technicianID);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    tickets.add(mapTicket(conn, rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public List<Ticket> fetchTicketsCreatedBy(int userID) {

        List<Ticket> tickets = new ArrayList<>();

        String sql = """
                SELECT t.*, u.EMAIL AS TECH_EMAIL
                FROM TICKETS t
                LEFT JOIN USERS u
                    ON t.ASSIGNED_TECHNICIAN_ID = u.USER_ID
                WHERE t.CREATED_BY_USER_ID = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    tickets.add(mapTicket(conn, rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public Ticket getTicketByID(int ticketID) {

        String sql = """
                SELECT t.*, u.EMAIL AS TECH_EMAIL
                FROM TICKETS t
                LEFT JOIN USERS u
                    ON t.ASSIGNED_TECHNICIAN_ID = u.USER_ID
                WHERE t.TICKET_ID = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketID);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapTicket(conn, rs);
                }
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
             PreparedStatement stmt = conn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, t.getTitle());
            stmt.setString(2, t.getDescription());
            stmt.setString(3, t.getCategory());
            stmt.setString(4, t.getPriority().name());
            stmt.setString(5, t.getStatus().name());
            stmt.setInt(6, t.getAssignedTechnicianID());
            stmt.setInt(7, t.getCreatedByUserID());
            stmt.setTimestamp(8, Timestamp.valueOf(t.getCreatedDate()));

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {

                if (keys.next()) {

                    int ticketID = keys.getInt(1);

                    for (Comment c : t.getComments()) {
                        saveComment(conn, ticketID, c);
                    }

                    return ticketID;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void updateTicket(Ticket t) {

        String sql = """
                UPDATE TICKETS
                SET TITLE=?,
                    DESCRIPTION=?,
                    CATEGORY=?,
                    PRIORITY=?,
                    STATUS=?,
                    ASSIGNED_TECHNICIAN_ID=?
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

    // =========================
    // MAPPING
    // =========================
    private Ticket mapTicket(Connection conn, ResultSet rs)
            throws SQLException {

        int ticketID = rs.getInt("TICKET_ID");

        Ticket ticket = new Ticket(
                ticketID,
                rs.getString("TITLE"),
                rs.getString("DESCRIPTION"),
                rs.getString("CATEGORY"),
                Priority.valueOf(rs.getString("PRIORITY")),
                rs.getInt("CREATED_BY_USER_ID"),
                Status.valueOf(rs.getString("STATUS")),
                rs.getTimestamp("CREATED_DATE").toLocalDateTime()
        );

        ticket.setAssignedTechnicianID(
                rs.getInt("ASSIGNED_TECHNICIAN_ID")
        );

        ticket.setAssignedTechnicianEmail(
                rs.getString("TECH_EMAIL")
        );

        ticket.setComments(
                fetchComments(conn, ticketID)
        );

        return ticket;
    }

    // =========================
    // COMMENTS
    // =========================
    private List<Comment> fetchComments(
            Connection conn,
            int ticketID) throws SQLException {

        List<Comment> comments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM COMMENTS
                WHERE TICKET_ID = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketID);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    comments.add(new Comment(
                            rs.getString("TITLE"),
                            rs.getString("DESCRIPTION"),
                            rs.getString("CREATED_BY"),
                            rs.getTimestamp("CREATED_DATE")
                                    .toLocalDateTime()
                    ));
                }
            }
        }

        return comments;
    }

    private void saveComment(
            Connection conn,
            int ticketID,
            Comment c) throws SQLException {

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
            stmt.setTimestamp(
                    5,
                    Timestamp.valueOf(c.getCreatedDate())
            );

            stmt.executeUpdate();
        }
    }
}