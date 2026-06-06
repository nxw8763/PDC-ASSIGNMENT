package gui.abstracts;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class AbstractDashboardPanel extends JPanel {

    protected final User currentUser;

    protected JPanel contentPanel;
    protected JPanel navigationPanel;
    protected JPanel topBarPanel;

    protected CardLayout cardLayout;

    private Runnable logoutAction;

    private boolean sidebarVisible = true;

    public AbstractDashboardPanel(User user) {

        this.currentUser = user;

        initialiseUI();
    }

    private void initialiseUI() {

        setLayout(new BorderLayout());

        createSidebar();
        createTopBar();
        createContentArea();
    }

    private void createSidebar() {

        navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
        navigationPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        navigationPanel.setPreferredSize(new Dimension(240, 0));

        add(navigationPanel, BorderLayout.WEST);
    }

    private void createTopBar() {

        topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton hamburgerButton = new JButton("☰");

        hamburgerButton.setFocusPainted(false);

        hamburgerButton.addActionListener(e -> {

            sidebarVisible = !sidebarVisible;

            navigationPanel.setVisible(sidebarVisible);

            revalidate();
            repaint();
        });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.add(hamburgerButton);

        JLabel userInfo = new JLabel(
                "<html><b>" + currentUser.getName() + "</b><br>"
                        + currentUser.getRole() + "</html>"
        );

        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> {
            if (logoutAction != null) {
                logoutAction.run();
            }
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        rightPanel.add(userInfo);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(logoutButton);

        topBarPanel.add(leftPanel, BorderLayout.WEST);
        topBarPanel.add(rightPanel, BorderLayout.EAST);

        add(topBarPanel, BorderLayout.NORTH);
    }

    private void createContentArea() {

        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);

        add(contentPanel, BorderLayout.CENTER);
    }

    protected JButton createNavButton(String title, String pageId) {

        JButton button = new JButton(title);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        button.addActionListener(e ->
                cardLayout.show(contentPanel, pageId)
        );

        navigationPanel.add(button);
        navigationPanel.add(Box.createVerticalStrut(10));

        return button;
    }

    protected void registerPage(String pageId, JPanel panel) {

        contentPanel.add(panel, pageId);
    }

    public void setLogoutAction(Runnable logoutAction) {
        this.logoutAction = logoutAction;
    }

    protected abstract void buildPages();

    protected abstract void buildNavigation();
}