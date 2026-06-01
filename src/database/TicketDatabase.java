package database;

import model.Comment;
import model.Priority;
import model.Status;
import model.Ticket;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class TicketDatabase {

    private static final String FILE = "data/tickets.txt";

    public List<Ticket> fetchTickets() {
        List<Ticket> tickets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");

                if (parts.length < 9) {
                    continue;
                }

                int ticketID = Integer.parseInt(parts[0]);
                String title = parts[1];
                String description = parts[2];
                String category = parts[3];

                Priority priority = Priority.valueOf(parts[4]);
                Status status = Status.valueOf(parts[5]);
                if(status == Status.CLOSED) {
                	
                	continue;
                }
                String assignedTech = parts[6];
                int createdByUserID = Integer.parseInt(parts[7]);
                LocalDateTime createdDate = LocalDateTime.parse(parts[8]);
                
                int commentCount = 0;
                List<Comment> comments = new ArrayList<>();

                    commentCount = Integer.parseInt(parts[9]);

                    int index = 10;

                    for (int i = 0; i < commentCount; i++) {

                        // SAFETY CHECK (VERY IMPORTANT)
                        if (index + 3 >= parts.length) {
                            System.out.println("Skipping corrupted comment data for ticket " + ticketID);
                            break;
                        }

                        String commentTitle = parts[index++];
                        String commentDesc = parts[index++];

                        String createdBy = parts[index++];

                        LocalDateTime createdCommentDate = LocalDateTime.parse(parts[index++]);

                        Comment comment = new Comment(
                                commentTitle,
                                commentDesc,
                                createdBy,
                                createdCommentDate
                        );

                        comments.add(comment);
                }
                Ticket ticket = new Ticket(
                        ticketID,
                        title,
                        description,
                        category,
                        priority,
                        createdByUserID,
                        status,
                        createdDate
                );
                ticket.setComments(comments);
                ticket.assignTechnician(assignedTech);
                tickets.add(ticket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public void saveTicket(Ticket ticket) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {

        	StringBuilder line = new StringBuilder();

        	line.append(ticket.getTicketID()).append("|");
        	line.append(ticket.getTitle()).append("|");
        	line.append(ticket.getDescription()).append("|");
        	line.append(ticket.getCategory()).append("|");
        	line.append(ticket.getPriority().name()).append("|");
        	line.append(ticket.getStatus().name()).append("|");
        	line.append(ticket.getAssignedTechnician()).append("|");
        	line.append(ticket.getCreatedByUserID()).append("|");
        	line.append(ticket.getCreatedDate()).append("|");
        	line.append(ticket.getComments().size());
        	for (Comment c : ticket.getComments()) {

        	    line.append("|")
        	        .append(c.getTitle()).append("|")
        	        .append(c.getDescription()).append("|")
        	        .append(c.getCreatedByUser()).append("|")
        	        .append(c.getCreatedDate());
        	}

        	writer.write(line.toString());
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void updateTicket(Ticket updatedTicket) {

        List<Ticket> tickets = fetchTickets();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, false))) {
            // false = overwrite file

            for (Ticket t : tickets) {

                if (t.getTicketID() == updatedTicket.getTicketID()) {
                    t = updatedTicket; // replace old ticket
                }

                saveTicket(t);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}