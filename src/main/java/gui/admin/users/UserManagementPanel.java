package gui.admin.users;

import model.Admin;
import model.User;
import service.UserManagementService;

import javax.swing.*;
import java.awt.*;

public class UserManagementPanel extends JPanel {

    private final UserManagementService userService;

    private final UserTableModel tableModel =
            new UserTableModel();

    private final Admin admin;
    
    private JTable table;

    public UserManagementPanel(
            Admin admin, UserManagementService userService
    ) {

        this.userService = userService;
        this.admin = admin;
        
        initialise();
        loadUsers();
    }

    private void initialise() {

        setLayout(new BorderLayout());

        table = new JTable(tableModel);

        table.getTableHeader().setReorderingAllowed(false);
        
        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        JPanel toolbar =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        JButton createButton =
                new JButton("Create");

        JButton editButton =
                new JButton("Edit");

        JButton deleteButton =
                new JButton("Delete");

        JButton refreshButton =
                new JButton("Refresh");

        toolbar.add(createButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.add(refreshButton);

        add(toolbar, BorderLayout.NORTH);

        createButton.addActionListener(
                e -> createUser()
        );

        editButton.addActionListener(
                e -> editUser()
        );

        deleteButton.addActionListener(
                e -> deleteUser()
        );

        refreshButton.addActionListener(
                e -> loadUsers()
        );
    }

    private void loadUsers() {

        tableModel.setUsers(
                userService.getAllUsers(admin)
        );
    }

    private User getSelectedUser() {

        int row = table.getSelectedRow();

        if (row < 0) {
            return null;
        }

        return tableModel.getUserAt(row);
    }

    private void createUser() {

        CreateUserDialog dialog =
                new CreateUserDialog(
                        SwingUtilities.getWindowAncestor(this),
                        admin,
                        userService
                );

        dialog.setVisible(true);

        loadUsers();
    }

    private void editUser() {

        User user = getSelectedUser();

        if (user == null) {
            return;
        }

        EditUserDialog dialog =
                new EditUserDialog(
                        SwingUtilities.getWindowAncestor(this),
                        admin,
                        user,
                        userService
                );

        dialog.setVisible(true);

        loadUsers();
    }

    private void deleteUser() {

        User user = getSelectedUser();

        if (user == null) {
            return;
        }

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete user?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        userService.removeUser(
        		admin,
                user.getUserID()
        );

        loadUsers();
    }
}