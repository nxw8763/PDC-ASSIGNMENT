package gui.login;

import gui.abstracts.AbstractDashboardPanel;
import gui.employee.EmployeeDashboardPanel;
import gui.technician.TechnicianDashboardPanel;
import gui.admin.AdminDashboardPanel;

import model.*;

import service.UserManagementService;
import service.TicketManagementService;
import service.CategoryService;

import javax.swing.*;

import dao.CategoryDAO;
import dao.TicketDAO;

public class DashboardLauncher {

    public static void launchDashboard(
            JFrame frame,
            User user,
            UserManagementService userService
    ) {

        AbstractDashboardPanel dashboard;


        
        if (user instanceof Employee emp) {

            dashboard = new EmployeeDashboardPanel(
                    emp,
                    new TicketManagementService(new TicketDAO()),
                    new CategoryService(new CategoryDAO())
            );

        } else if (user instanceof Technician tech) {

            dashboard = new TechnicianDashboardPanel(
                    tech,
                    new TicketManagementService(new TicketDAO())
            );

        } else if (user instanceof Admin admin) {

            dashboard = new AdminDashboardPanel(
                    admin,
                    userService,
                    new TicketManagementService(new TicketDAO()),
                    new CategoryService(new CategoryDAO())
            );
        } else {
        	return;
        }

        dashboard.setLogoutAction(() -> {

            frame.setContentPane(
                    new LoginPanel(
                            frame,
                            userService
                    )
            );

            frame.revalidate();
            frame.repaint();
        });

        frame.setContentPane(dashboard);
        frame.revalidate();
        frame.repaint();
    }
}