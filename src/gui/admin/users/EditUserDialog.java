package gui.admin.users;

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
            User user,
            UserManagementService userService
    ) {

        super(owner, "Edit User", ModalityType.APPLICATION_MODAL);

        this.user = user;
        this.userService = userService;

        initialise();
    }

    private void initialise() {

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
                e -> saveChanges()
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

    private void saveChanges() {

        int id = user.getUserID();

        userService.updateUserField(
                id,
                "username",
                form.getUsernameField().getText()
        );

        userService.updateUserField(
                id,
                "name",
                form.getNameField().getText()
        );

        userService.updateUserField(
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
                id,
                roleChoice
        );

        dispose();
    }
}