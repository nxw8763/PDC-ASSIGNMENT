package serviceDeskTests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import dao.CategoryDAO;
import dao.TicketDAO;
import dao.UserDAO;
import database.DatabaseInitializer;
import model.enums.Priority;
import model.enums.Status;
import model.tickets.Ticket;
import model.users.Admin;
import model.users.Employee;
import model.users.Technician;
import model.users.User;
import service.CategoryService;
import service.TicketService;
import service.UserService;

public class ServiceDeskTest {

    private static UserDAO userDAO;
    private static TicketDAO ticketDAO;
    private static CategoryDAO categoryDAO;

    private static UserService userService;
    private static TicketService ticketService;
    private static CategoryService categoryService;

    private static Admin admin;
    private static Technician technician;
    private static Employee employee;

    @BeforeClass
    public static void setup() {

        try {
            DatabaseInitializer.resetDatabase();
        } catch (Exception ignored) {
        }

        DatabaseInitializer.initialize();

        userDAO = new UserDAO();
        ticketDAO = new TicketDAO();
        categoryDAO = new CategoryDAO();

        userService = new UserService(userDAO);
        ticketService = new TicketService(ticketDAO);
        categoryService = new CategoryService(categoryDAO);

        admin =
                new Admin(
                        1,
                        "admin",
                        "System Admin",
                        "admin@sys.com"
                );

        technician =
                new Technician(
                        2,
                        "tech1",
                        "John Tech",
                        "tech1@sys.com"
                );

        employee =
                new Employee(
                        3,
                        "emp1",
                        "Jane Employee",
                        "emp1@sys.com"
                );
    }

    // =====================================================
    // USER AUTHENTICATION
    // =====================================================

    @Test
    public void authenticate_ValidAdmin_ReturnsAdminUser() {

        User user =
                userDAO.authenticate(
                        "admin",
                        "admin123"
                );

        assertNotNull(user);
        assertEquals(
                "ADMINISTRATOR",
                user.getRole()
        );
    }

    @Test
    public void authenticate_InvalidPassword_ReturnsNull() {

        User user =
                userDAO.authenticate(
                        "admin",
                        "wrongPassword"
                );

        assertNull(user);
    }

    @Test
    public void authenticate_InvalidUsername_ReturnsNull() {

        User user =
                userDAO.authenticate(
                        "doesNotExist",
                        "admin123"
                );

        assertNull(user);
    }

    // =====================================================
    // CATEGORYS
    // =====================================================

    @Test
    public void getCategories_ReturnsSeededCategories() {

        List<String> categories =
                categoryService.getCategories();

        assertTrue(categories.contains("Hardware"));
        assertTrue(categories.contains("Software"));
        assertTrue(categories.contains("Network"));
    }

    @Test
    public void addCategory_AsAdmin_CategoryExistsAfterInsert() {

        String category =
                "JUnitCategory";

        categoryService.addCategory(
                admin,
                category
        );

        List<String> categories =
                categoryService.getCategories();

        assertTrue(
                categories.contains(category)
        );
    }

    @Test(expected = SecurityException.class)
    public void addCategory_AsEmployee_ThrowsSecurityException() {

        categoryService.addCategory(
                employee,
                "IllegalCategory"
        );
    }

    @Test(expected = SecurityException.class)
    public void deleteCategory_AsTechnician_ThrowsSecurityException() {

        categoryService.deleteCategory(
                technician,
                "Hardware"
        );
    }

    @Test
    public void deleteCategory_AsAdmin_RemovesCategory() {

        String category =
                "DeleteMe";

        categoryService.addCategory(
                admin,
                category
        );

        categoryService.deleteCategory(
                admin,
                category
        );

        List<String> categories =
                categoryService.getCategories();

        assertFalse(
                categories.contains(category)
        );
    }

    // =====================================================
    // TICKET CREATION
    // =====================================================

    @Test
    public void createTicket_CreatesTicketWithOpenStatus() {

        Ticket ticket =
                ticketService.createTicket(
                        "Printer Broken",
                        "Printer not working",
                        "Hardware",
                        Priority.HIGH,
                        employee
                );

        assertTrue(
                ticket.getTicketID() > 0
        );

        Ticket stored =
                ticketDAO.getTicketByID(
                        ticket.getTicketID()
                );

        assertNotNull(stored);

        assertEquals(
                Status.OPEN,
                stored.getStatus()
        );

        assertEquals(
                "Printer Broken",
                stored.getTitle()
        );
    }

    @Test
    public void employeeCanOnlySeeOwnTickets() {

        Ticket created =
                ticketService.createTicket(
                        "Employee Ticket",
                        "Description",
                        "Software",
                        Priority.LOW,
                        employee
                );

        List<Ticket> visible =
                ticketService.getVisibleTickets(
                        employee
                );

        boolean found = visible.stream()
                .anyMatch(t ->
                        t.getTicketID() ==
                                created.getTicketID());

        assertTrue(found);

        for (Ticket ticket : visible) {

            assertEquals(
                    employee.getUserID(),
                    ticket.getCreatedByUserID()
            );
        }
    }

    // =====================================================
    // ASSIGNMENT
    // =====================================================

    @Test
    public void technicianCanAssignOpenTicketToSelf() {

        Ticket ticket =
                ticketService.createTicket(
                        "Network Issue",
                        "Cannot connect",
                        "Network",
                        Priority.MEDIUM,
                        employee
                );

        ticketService.assignTicket(
                ticket.getTicketID(),
                technician.getUserID(),
                technician.getEmail(),
                technician
        );

        Ticket updated =
                ticketDAO.getTicketByID(
                        ticket.getTicketID()
                );

        assertEquals(
                Status.ASSIGNED,
                updated.getStatus()
        );

        assertEquals(
                technician.getUserID(),
                updated.getAssignedTechnicianID()
        );
    }

    @Test
    public void technicianCannotModifyClosedTicket() {

        Ticket ticket =
                ticketService.createTicket(
                        "Closed Ticket Test",
                        "Description",
                        "Hardware",
                        Priority.LOW,
                        employee
                );

        ticketService.assignTicket(
                ticket.getTicketID(),
                technician.getUserID(),
                technician.getEmail(),
                technician
        );

        ticketService.closeTicket(
                ticket.getTicketID(),
                admin
        );

        ticketService.updatePriority(
                ticket.getTicketID(),
                Priority.CRITICAL,
                technician
        );

        Ticket updated =
                ticketDAO.getTicketByID(
                        ticket.getTicketID()
                );

        assertNotEquals(
                Priority.CRITICAL,
                updated.getPriority()
        );
    }

    // =====================================================
    // COMMENTS
    // =====================================================

    @Test
    public void addComment_IncreasesCommentCount() {

        Ticket ticket =
                ticketService.createTicket(
                        "Comment Test",
                        "Description",
                        "Software",
                        Priority.MEDIUM,
                        employee
                );

        ticketService.addComment(
                ticket.getTicketID(),
                "Investigation",
                "Looking into issue",
                technician
        );

        Ticket updated =
                ticketDAO.getTicketByID(
                        ticket.getTicketID()
                );

        assertEquals(
                1,
                updated.getComments().size()
        );
    }

    // =====================================================
    // USER MANAGEMENT
    // =====================================================

    @Test
    public void createUser_Admin_CreatesUserSuccessfully() {

        userService.createUser(
                admin,
                "JUnit User",
                "junitUser",
                "password",
                "EMPLOYEE",
                "junit@test.com"
        );

        User user =
                userDAO.authenticate(
                        "junitUser",
                        "password"
                );

        assertNotNull(user);
    }

    @Test
    public void removeUser_ValidUser_ReturnsTrue() {

        userService.createUser(
                admin,
                "Delete User",
                "deleteUser",
                "password",
                "EMPLOYEE",
                "delete@test.com"
        );

        User created =
                userDAO.authenticate(
                        "deleteUser",
                        "password"
                );

        boolean result =
                userService.removeUser(
                        admin,
                        created.getUserID()
                );

        assertTrue(result);

        User removed =
                userDAO.getUserByID(
                        created.getUserID()
                );

        assertNull(removed);
    }

    @Test
    public void removeUser_InvalidId_ReturnsFalse() {

        boolean result =
                userService.removeUser(
                        admin,
                        -1
                );

        assertFalse(result);
    }
}