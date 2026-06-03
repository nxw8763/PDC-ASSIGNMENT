package gui.admin;

import model.User;
import service.UserManagementService;

import javax.swing.*;

import gui.admin.users.CreateUserDialog;

import java.awt.*;
import java.util.List;

public class AdminUsersPanel extends JPanel {

    private final UserManagementService userService;

    private final JTextArea displayArea = new JTextArea();

    public AdminUsersPanel(UserManagementService userService) {

        this.userService = userService;

        initialise();
        loadUsers();
    }

    private void initialise() {

        setLayout(new BorderLayout());

        displayArea.setEditable(false);

        JScrollPane scrollPane =
                new JScrollPane(displayArea);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton refresh = new JButton("Refresh");
        JButton create = new JButton("Create User");
        JButton edit = new JButton("Edit User");
        JButton delete = new JButton("Delete User");

        buttons.add(refresh);
        buttons.add(create);
        buttons.add(edit);
        buttons.add(delete);

        add(buttons, BorderLayout.NORTH);

        refresh.addActionListener(e -> loadUsers());
        create.addActionListener(e -> createUser());
        edit.addActionListener(e -> editUser());
        delete.addActionListener(e -> deleteUser());
    }

    private void loadUsers() {

        List<User> users = userService.getAllUsers();

        StringBuilder sb = new StringBuilder();

        sb.append("===== USERS =====\n\n");

        for (User u : users) {

            sb.append(u.getUserID()).append(" | ")
              .append(u.getUsername()).append(" | ")
              .append(u.getRole()).append(" | ")
              .append(u.getName()).append(" | ")
              .append(u.getEmail())
              .append("\n");
        }

        displayArea.setText(sb.toString());
    }

    private User getSelectedByPrompt() {

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter User ID:"
                );

        if (input == null) return null;

        try {
            int id = Integer.parseInt(input);

            return userService.getAllUsers()
                    .stream()
                    .filter(u -> u.getUserID() == id)
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            return null;
        }
    }

    private void createUser() {

        new CreateUserDialog(
                SwingUtilities.getWindowAncestor(this),
                userService
        ).setVisible(true);

        loadUsers();
    }

    private void editUser() {

        User user = getSelectedByPrompt();
        if (user == null) return;

        new UserEditorDialog(
                SwingUtilities.getWindowAncestor(this),
                user,
                userService
        ).setVisible(true);

        loadUsers();
    }

    private void deleteUser() {

        User user = getSelectedByPrompt();
        if (user == null) return;

        userService.removeUser(user.getUserID());

        loadUsers();
    }
}