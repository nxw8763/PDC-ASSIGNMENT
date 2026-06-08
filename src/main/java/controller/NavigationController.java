package controller;

import javax.swing.JFrame;

import main.AppContext;
import gui.abstracts.AbstractDashboardPanel;
import gui.login.DashboardFactory;
import gui.login.LoginPanel;
import model.users.User;

public class NavigationController {

    private final JFrame frame;
    private final AppContext context;

    public NavigationController(
            JFrame frame,
            AppContext context
    ) {
        this.frame = frame;
        this.context = context;
    }

    public void showLogin() {

        frame.setContentPane(
                new LoginPanel(
                        this,
                        context.getLoginController()
                )
        );

        refreshFrame();
    }

    public void showDashboard(User user) {

        AbstractDashboardPanel dashboard =
                DashboardFactory.createDashboard(
                        user,
                        context,
                        this
                );

        frame.setContentPane(dashboard);

        refreshFrame();
    }

    private void refreshFrame() {

        frame.revalidate();
        frame.repaint();
    }
}