package gui.login;

import gui.abstracts.AbstractDashboardPanel;
import gui.employee.EmployeeDashboardPanel;
import gui.technician.TechnicianDashboardPanel;
import gui.admin.AdminDashboardPanel;

import model.users.Admin;
import model.users.Employee;
import model.users.Technician;
import model.users.User;
import service.UserService;
import service.TicketService;
import service.CategoryService;

import dao.CategoryDAO;
import dao.TicketDAO;

public class DashboardFactory {

    public static AbstractDashboardPanel createDashboard(
            User user,
            TicketService ticketService,
            CategoryService categoryService,
            UserService userService
    ) {

        if (user instanceof Employee emp) {
            return new EmployeeDashboardPanel(
                    emp,
                    ticketService,
                    categoryService
            );
        }

        if (user instanceof Technician tech) {
            return new TechnicianDashboardPanel(
                    tech,
                   	ticketService
            );
        }

        if (user instanceof Admin admin) {
            return new AdminDashboardPanel(
                    admin,
                    userService,
                    ticketService,
                    categoryService
            );
        }

        throw new IllegalArgumentException("Unknown user type");
    }
}