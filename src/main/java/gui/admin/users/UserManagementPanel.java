package gui.admin.users;

import controller.UserController;
import dto.UserDTO;
import model.users.Admin;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserManagementPanel
        extends JPanel
        implements UserManagementView {

    private final Admin admin;

    private final UserController controller;

    private final UserTableModel tableModel =
            new UserTableModel();

    private JTable table;

    public UserManagementPanel(
            Admin admin,
            UserController controller) {

        this.admin = admin;
        this.controller = controller;

        initialise();

        controller.loadUsers(
                admin,
                this
        );
    }

    private void initialise() {

        setLayout(
                new BorderLayout()
        );

        table =
                new JTable(
                        tableModel
                );

        table.getTableHeader()
                .setReorderingAllowed(
                        false
                );

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

        add(
                toolbar,
                BorderLayout.NORTH
        );

        createButton.addActionListener(
                e -> controller.openCreateUser(
                        admin,
                        this
                )
        );

        editButton.addActionListener(
                e -> {

                    UserDTO user =
                            getSelectedUser();

                    if (user == null) {
                        return;
                    }

                    controller.openEditUser(
                            admin,
                            user,
                            this
                    );
                }
        );

        deleteButton.addActionListener(
                e -> {

                    UserDTO user =
                            getSelectedUser();

                    if (user == null) {
                        return;
                    }

                    controller.deleteUser(
                            admin,
                            user.getUserId(),
                            this
                    );
                }
        );

        refreshButton.addActionListener(
                e -> controller.loadUsers(
                        admin,
                        this
                )
        );
    }

    private UserDTO getSelectedUser() {

        int row =
                table.getSelectedRow();

        if (row < 0) {
            return null;
        }

        row =
                table.convertRowIndexToModel(
                        row
                );

        return tableModel.getUserAt(
                row
        );
    }

    @Override
    public void displayUsers(
            List<UserDTO> users) {

        tableModel.setUsers(
                users
        );
    }

    @Override
    public Window getParentWindow() {

        return SwingUtilities
                .getWindowAncestor(this);
    }

    @Override
    public void showMessage(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message
        );
    }

    @Override
    public void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}