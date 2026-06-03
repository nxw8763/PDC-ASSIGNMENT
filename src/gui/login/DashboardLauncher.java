package gui.login;

import javax.swing.JFrame;

import database.UserDatabase;
import gui.abstracts.AbstractDashboardPanel;
import gui.employee.EmployeeDashboardPanel;
import gui.technician.TechnicianDashboardPanel;
import gui.admin.AdminDashboardPanel;
import model.Employee;
import model.Technician;
import model.User;
import service.CategoryService;
import service.TicketManagementService;
import service.UserManagementService;

public class DashboardLauncher {

    public static void launchDashboard(
            JFrame frame,
            User user,
            UserDatabase userDb) {

        AbstractDashboardPanel dashboard;

        if (user instanceof Employee) {

            dashboard = new EmployeeDashboardPanel(
                    (Employee) user,
                    userDb
            );

        } else if (user instanceof Technician) {

            dashboard = new TechnicianDashboardPanel(
                    (Technician) user,
                    userDb
            );

        } else {

            UserManagementService userService =
                    new UserManagementService(userDb);

            TicketManagementService ticketService =
                    new TicketManagementService(userDb);

            CategoryService categoryService =
                    new CategoryService();

            dashboard = new AdminDashboardPanel(
                    user,
                    userService,
                    ticketService,
                    categoryService
            );
        }

        dashboard.setLogoutAction(() -> {

            frame.setContentPane(
                    new LoginPanel(frame)
            );

            frame.revalidate();
            frame.repaint();
        });

        frame.setContentPane(dashboard);

        frame.revalidate();
        frame.repaint();
    }
}