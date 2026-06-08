package gui.login;

import controller.NavigationController;

import gui.abstracts.AbstractDashboardPanel;
import gui.admin.AdminDashboardPanel;
import gui.employee.EmployeeDashboardPanel;
import gui.technician.TechnicianDashboardPanel;

import main.AppContext;

import model.users.Admin;
import model.users.Employee;
import model.users.Technician;
import model.users.User;

public class DashboardFactory {

    public static AbstractDashboardPanel createDashboard(
            User user,
            AppContext context,
            NavigationController navigation
    ) {

        AbstractDashboardPanel dashboard;

        if (user instanceof Employee employee) {

            dashboard =
                    new EmployeeDashboardPanel(
                            employee,
                            context
                    );

        } else if (user instanceof Technician technician) {

            dashboard =
                    new TechnicianDashboardPanel(
                            technician,
                            context
                    );

        } else if (user instanceof Admin admin) {

            dashboard =
                    new AdminDashboardPanel(
                            admin,
                            context
                    );

        } else {

            throw new IllegalArgumentException(
                    "Unknown user type."
            );
        }

        dashboard.setLogoutAction(
                navigation::showLogin
        );

        return dashboard;
    }
}