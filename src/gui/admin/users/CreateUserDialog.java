package gui.admin.users;

import service.UserManagementService;

import javax.swing.*;
import java.awt.*;

public class CreateUserDialog extends JDialog {

    private final UserManagementService userService;

    private final UserFormPanel form =
            new UserFormPanel();

    public CreateUserDialog(
            Window owner,
            UserManagementService userService
    ) {

        super(owner, "Create User", ModalityType.APPLICATION_MODAL);

        this.userService = userService;

        initialise();
    }

    private void initialise() {

        setLayout(new BorderLayout());

        add(form, BorderLayout.CENTER);

        JButton createButton =
                new JButton("Create User");

        createButton.addActionListener(
                e -> createUser()
        );

        JPanel bottom = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT
                )
        );

        bottom.add(createButton);

        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    private void createUser() {

        String username =
                form.getUsernameField().getText();

        String name =
                form.getNameField().getText();

        String email =
                form.getEmailField().getText();

        String password =
                new String(
                        form.getPasswordField().getPassword()
                );

        String role = (String) form.getRoleBox().getSelectedItem();

        userService.createUser(
                name,
                username,
                password,
                role,
                email
        );

        dispose();
    }
}