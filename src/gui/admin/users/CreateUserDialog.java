package gui.admin.users;

import service.UserManagementService;

import javax.swing.*;

import model.Admin;

import java.awt.*;

public class CreateUserDialog extends JDialog {

    private final UserManagementService userService;

    private final UserFormPanel form =
            new UserFormPanel();

    public CreateUserDialog(
            Window owner,
            Admin admin,
            UserManagementService userService
    ) {

        super(owner, "Create User", ModalityType.APPLICATION_MODAL);

        this.userService = userService;

        initialise(admin);
    }

    private void initialise(Admin admin) {

        setLayout(new BorderLayout());

        add(form, BorderLayout.CENTER);

        JButton createButton =
                new JButton("Create User");

        createButton.addActionListener(
                e -> createUser(admin)
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

    private void createUser(Admin admin) {

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
        		admin,
                name,
                username,
                password,
                role,
                email
        );

        dispose();
    }
}