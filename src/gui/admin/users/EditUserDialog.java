package gui.admin.users;

import model.Admin;
import model.User;
import service.UserManagementService;

import javax.swing.*;
import java.awt.*;

public class EditUserDialog extends JDialog {

    private final User user;
    private final UserManagementService userService;

    private final UserFormPanel form =
            new UserFormPanel();

    public EditUserDialog(
            Window owner,
            Admin admin,
            User user,
            UserManagementService userService
    ) {

        super(owner, "Edit User", ModalityType.APPLICATION_MODAL);

        this.user = user;
        this.userService = userService;

        initialise(admin);
    }

    private void initialise(Admin admin) {

        form.getUsernameField()
                .setText(user.getUsername());

        form.getNameField()
                .setText(user.getName());

        form.getEmailField()
                .setText(user.getEmail());

        form.getRoleBox()
                .setSelectedItem(user.getRole());

        setLayout(new BorderLayout());

        add(form, BorderLayout.CENTER);

        JButton saveButton =
                new JButton("Save");

        saveButton.addActionListener(
                e -> saveChanges(admin)
        );

        JPanel bottom = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT
                )
        );

        bottom.add(saveButton);

        add(bottom, BorderLayout.SOUTH);

        pack();

        setLocationRelativeTo(getOwner());
    }

    private void saveChanges(Admin admin) {

        int id = user.getUserID();

        userService.updateUserField(
        		admin,
                id,
                "username",
                form.getUsernameField().getText()
        );

        userService.updateUserField(
        		admin,
                id,
                "name",
                form.getNameField().getText()
        );

        userService.updateUserField(
        		admin,
                id,
                "email",
                form.getEmailField().getText()
        );

        String password =
                new String(
                        form.getPasswordField().getPassword()
                );

        if (!password.isBlank()) {

            userService.updateUserField(
            		admin,
                    id,
                    "password",
                    password
            );
        }

        String role =
                (String) form.getRoleBox()
                        .getSelectedItem();

        String roleChoice =
                switch (role) {

                    case "EMPLOYEE" -> "1";
                    case "TECHNICIAN" -> "2";
                    default -> "3";
                };

        userService.updateUserRole(
        		admin,
                id,
                roleChoice
        );

        dispose();
    }
}