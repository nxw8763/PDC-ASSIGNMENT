package controller;

import dto.CommentDTO;
import dto.TicketCardDTO;
import dto.TicketDetailsDTO;
import gui.admin.AdminTicketDetailsDialog;
import gui.employee.CreateTicketView;
import gui.employee.EmployeeTicketDetailsDialog;
import gui.employee.MyTicketsView;
import gui.employee.TicketDetailsView;
import gui.technician.TechnicianTicketDetailsDialog;
import gui.tickets.TicketBoardView;
import gui.tickets.TicketCard;
import model.enums.Priority;
import model.enums.Status;
import model.tickets.Comment;
import model.tickets.Ticket;
import model.users.Admin;
import model.users.Employee;
import model.users.Technician;
import model.users.User;
import service.TicketService;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(
            TicketService ticketService,
            UserService userService) {

        this.ticketService = ticketService;
        this.userService = userService;
    }

    /*
     * =====================================================
     * EMPLOYEE TICKETS
     * =====================================================
     */

    public void loadEmployeeTickets(
            Employee employee,
            MyTicketsView view,
            Component parent
    ) {

        List<Ticket> tickets =
                ticketService.getVisibleTickets(
                        employee
                );

        sortTickets(
                tickets,
                view.getSelectedSort()
        );

        List<Component> cards =
                new ArrayList<>();

        for (Ticket ticket : tickets) {

            TicketCardDTO dto =
                    toTicketCardDTO(
                            ticket
                    );

            cards.add(
                    new TicketCard(
                            dto,
                            () -> openTicket(
                                    dto.getTicketId(),
                                    employee,
                                    parent,
                                    () -> loadEmployeeTickets(
                                            employee,
                                            view,
                                            parent
                                    )
                            )
                    )
            );
        }

        view.displayTickets(
                cards
        );
    }
    
    /*
     * =====================================================
     * BOARD
     * =====================================================
     */

    public void loadBoard(
            User user,
            TicketBoardView view,
            Component parent
    ) {

        List<Ticket> tickets =
                ticketService.getVisibleTickets(user);

        List<Component> openCards =
                new ArrayList<>();

        List<Component> assignedCards =
                new ArrayList<>();

        List<Component> progressCards =
                new ArrayList<>();

        List<Component> resolvedCards =
                new ArrayList<>();

        for (Ticket ticket : tickets) {

            TicketCardDTO dto =
                    toTicketCardDTO(ticket);

            TicketCard card =
                    new TicketCard(
                            dto,
                            () -> openTicket(
                                    dto.getTicketId(),
                                    user,
                                    parent,
                                    () -> loadBoard(
                                            user,
                                            view,
                                            parent
                                    )
                            )
                    );

            switch (ticket.getStatus()) {

                case OPEN ->
                        openCards.add(card);

                case ASSIGNED ->
                        assignedCards.add(card);

                case IN_PROGRESS ->
                        progressCards.add(card);

                case RESOLVED ->
                        resolvedCards.add(card);
            }
        }

        view.displayOpenTickets(openCards);
        view.displayAssignedTickets(assignedCards);
        view.displayInProgressTickets(progressCards);
        view.displayResolvedTickets(resolvedCards);
    }

    /*
     * =====================================================
     * OPEN TICKET
     * =====================================================
     */

    private void openTicket(
            int ticketId,
            User user,
            Component parent,
            Runnable refreshAction) {

        Window window =
                SwingUtilities.getWindowAncestor(parent);

        TicketDetailsView dialog;

        if (user instanceof Employee employee) {

            EmployeeTicketDetailsDialog d =
                    new EmployeeTicketDetailsDialog(
                            window,
                            ticketId
                    );

            d.setAddCommentAction(
                    () -> addComment(
                            ticketId,
                            employee,
                            d
                    )
            );

            dialog = d;
        }
        else if (user instanceof Technician technician) {

            TechnicianTicketDetailsDialog d =
                    new TechnicianTicketDetailsDialog(
                            window,
                            ticketId
                    );

            d.setAssignAction(
                    () -> assignTicket(
                            ticketId,
                            technician,
                            d
                    )
            );

            d.setUnassignAction(
                    () -> unassignTicket(
                            ticketId,
                            technician,
                            d
                    )
            );

            d.setStatusAction(
                    () -> changeStatus(
                            ticketId,
                            technician,
                            d
                    )
            );

            d.setPriorityAction(
                    () -> changePriority(
                            ticketId,
                            technician,
                            d
                    )
            );

            d.setCommentAction(
                    () -> addComment(
                            ticketId,
                            technician,
                            d
                    )
            );

            dialog = d;
        }
        else if (user instanceof Admin admin) {

            AdminTicketDetailsDialog d =
                    new AdminTicketDetailsDialog(
                            window,
                            ticketId
                    );

            d.setAssignAction(
                    () -> assignTicket(
                            ticketId,
                            admin,
                            d
                    )
            );

            d.setUnassignAction(
                    () -> unassignTicket(
                            ticketId,
                            admin,
                            d
                    )
            );

            d.setStatusAction(
                    () -> changeStatus(
                            ticketId,
                            admin,
                            d
                    )
            );

            d.setPriorityAction(
                    () -> changePriority(
                            ticketId,
                            admin,
                            d
                    )
            );

            d.setCommentAction(
                    () -> addComment(
                            ticketId,
                            admin,
                            d
                    )
            );

            d.setCloseAction(
                    () -> closeTicket(
                            ticketId,
                            admin,
                            d
                    )
            );

            d.setReopenAction(
                    () -> reopenTicket(
                            ticketId,
                            admin,
                            d
                    )
            );

            dialog = d;
        }
        else {

            return;
        }

        loadTicketDetails(
                ticketId,
                dialog
        );

        ((JDialog) dialog).setVisible(true);

        refreshAction.run();
    }

    /*
     * =====================================================
     * CREATE TICKET
     * =====================================================
     */

    public void createTicket(
            Employee employee,
            CreateTicketView view) {

        String title =
                view.getTicketTitle();

        String description =
                view.getTicketDescription();

        if (title == null
                || title.isBlank()) {

            view.showError(
                    "Title is required."
            );

            return;
        }

        if (description == null
                || description.isBlank()) {

            view.showError(
                    "Description is required."
            );

            return;
        }

        try {

            Ticket ticket =
                    ticketService.createTicket(
                            title,
                            description,
                            view.getSelectedCategory(),
                            view.getSelectedPriority(),
                            employee
                    );

            view.showMessage(
                    "Ticket Created. ID: "
                            + ticket.getTicketID()
            );

            view.clearForm();

        } catch (IllegalArgumentException ex) {

            view.showError(
                    ex.getMessage()
            );
        }
    }

    /*
     * =====================================================
     * TICKET DETAILS
     * =====================================================
     */

    public void loadTicketDetails(
            int ticketId,
            TicketDetailsView view) {

        Ticket ticket =
                ticketService.getTicketById(
                        ticketId
                );

        if (ticket == null) {

            view.showError(
                    "Ticket not found."
            );

            return;
        }

        List<CommentDTO> comments =
                new ArrayList<>();

        for (Comment comment :
                ticket.getComments()) {

            comments.add(
                    new CommentDTO(
                            comment.getTitle(),
                            comment.getDescription(),
                            comment.getCreatedByUser(),
                            comment.getCreatedDate()
                                    .format(
                                            DateTimeFormatter.ofPattern(
                                                    "dd/MM/yyyy HH:mm"
                                            )
                                    )
                    )
            );
        }

        TicketDetailsDTO dto =
                new TicketDetailsDTO(
                        ticket.getTicketID(),
                        ticket.getTitle(),
                        ticket.getCategory(),
                        ticket.getPriority().toString(),
                        ticket.getStatus().toString(),
                        ticket.getAssignedTechnicianEmail() == null
                                ? "Unassigned"
                                : ticket.getAssignedTechnicianEmail(),
                        ticket.getCreatedDate()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "dd/MM/yyyy HH:mm"
                                        )
                                ),
                        ticket.getDescription(),
                        comments
                );

        view.displayTicket(
                dto
        );
    }

    /*
     * =====================================================
     * COMMENTS
     * =====================================================
     */

    public void addComment(
            int ticketId,
            User user,
            TicketDetailsView view) {

        String title =
                JOptionPane.showInputDialog(
                        null,
                        "Comment Title"
                );

        if (title == null
                || title.isBlank()) {

            return;
        }

        String description =
                JOptionPane.showInputDialog(
                        null,
                        "Comment Description"
                );

        if (description == null
                || description.isBlank()) {

            return;
        }

        ticketService.addComment(
                ticketId,
                title,
                description,
                user
        );

        loadTicketDetails(
                ticketId,
                view
        );
    }

    /*
     * =====================================================
     * ASSIGNMENT
     * =====================================================
     */

    public void assignTicket(
            int ticketId,
            User user,
            TicketDetailsView view) {

        if (user instanceof Technician technician) {

            ticketService.assignTicket(
                    ticketId,
                    technician.getUserID(),
                    technician.getEmail(),
                    technician
            );
        }
        else if (user instanceof Admin admin) {

            List<Technician> technicians =
                    userService.getAllTechnicians(admin);

            JComboBox<Technician> combo =
                    new JComboBox<>(
                            technicians.toArray(
                                    new Technician[0]
                            )
                    );

            int result =
                    JOptionPane.showConfirmDialog(
                            null,
                            combo,
                            "Select Technician",
                            JOptionPane.OK_CANCEL_OPTION
                    );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            Technician technician =
                    (Technician) combo.getSelectedItem();

            if (technician == null) {
                return;
            }

            ticketService.assignTicket(
                    ticketId,
                    technician.getUserID(),
                    technician.getEmail(),
                    admin
            );
        }

        loadTicketDetails(
                ticketId,
                view
        );
    }

    public void unassignTicket(
            int ticketId,
            User user,
            TicketDetailsView view) {

        ticketService.unassignTicket(
                ticketId,
                user
        );

        loadTicketDetails(
                ticketId,
                view
        );
    }

    /*
     * =====================================================
     * STATUS
     * =====================================================
     */

    public void changeStatus(
            int ticketId,
            User user,
            TicketDetailsView view) {

        Status[] statuses;

        if (user instanceof Technician) {

            statuses = new Status[]{
                    Status.ASSIGNED,
                    Status.IN_PROGRESS,
                    Status.RESOLVED
            };
        }
        else {

            statuses = Status.values();
        }

        Status selected =
                (Status) JOptionPane.showInputDialog(
                        null,
                        "Select Status",
                        "Status",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        statuses,
                        statuses[0]
                );

        if (selected == null) {
            return;
        }

        ticketService.updateStatus(
                ticketId,
                selected,
                user
        );

        loadTicketDetails(
                ticketId,
                view
        );
    }

    /*
     * =====================================================
     * PRIORITY
     * =====================================================
     */

    public void changePriority(
            int ticketId,
            User user,
            TicketDetailsView view) {

        Priority priority =
                (Priority) JOptionPane.showInputDialog(
                        null,
                        "Select Priority",
                        "Priority",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        Priority.values(),
                        Priority.MEDIUM
                );

        if (priority == null) {
            return;
        }

        ticketService.updatePriority(
                ticketId,
                priority,
                user
        );

        loadTicketDetails(
                ticketId,
                view
        );
    }

    /*
     * =====================================================
     * ADMIN ONLY
     * =====================================================
     */

    public void closeTicket(
            int ticketId,
            Admin admin,
            TicketDetailsView view) {

        ticketService.closeTicket(
                ticketId,
                admin
        );

        loadTicketDetails(
                ticketId,
                view
        );
    }

    public void reopenTicket(
            int ticketId,
            Admin admin,
            TicketDetailsView view) {

        ticketService.unassignTicket(
                ticketId,
                admin
        );

        loadTicketDetails(
                ticketId,
                view
        );
    }

    /*
     * =====================================================
     * SORTING
     * =====================================================
     */

    private void sortTickets(
            List<Ticket> tickets,
            String sort) {

        switch (sort) {

            case "Newest First" ->
                    tickets.sort(
                            Comparator.comparing(
                                    Ticket::getCreatedDate
                            ).reversed()
                    );

            case "Oldest First" ->
                    tickets.sort(
                            Comparator.comparing(
                                    Ticket::getCreatedDate
                            )
                    );

            case "Priority" ->
                    tickets.sort(
                            (a, b) ->
                                    Integer.compare(
                                            b.getPriority().ordinal(),
                                            a.getPriority().ordinal()
                                    )
                    );

            case "Status" ->
                    tickets.sort(
                            Comparator.comparing(
                                    Ticket::getStatus
                            )
                    );

            case "Category" ->
                    tickets.sort(
                            Comparator.comparing(
                                    Ticket::getCategory
                            )
                    );
        }
    }
    
    // HELPER
    private TicketCardDTO toTicketCardDTO(
            Ticket ticket
    ) {

        return new TicketCardDTO(
                ticket.getTicketID(),
                ticket.getTitle(),
                ticket.getCategory(),
                ticket.getPriority().toString(),
                ticket.getStatus().toString(),
                ticket.getAssignedTechnicianEmail()
        );
    }
}