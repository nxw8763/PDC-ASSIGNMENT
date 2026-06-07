package gui.admin.users;

import service.UserService;

import javax.swing.*;

import model.users.Admin;

import java.awt.*;

public class CreateUserDialog extends JDialog {

    private final UserService userService;

    private final UserFormPanel form =
            new UserFormPanel();

    public CreateUserDialog(
            Window owner,
            Admin admin,
            UserService userService
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

        try {
	        userService.createUser(
	        		admin,
	                name,
	                username,
	                password,
	                role,
	                email
	        		);
	        dispose();
	    } catch (IllegalArgumentException ex) {
	
	        JOptionPane.showMessageDialog(
	                this,
	                ex.getMessage(),
	                "Validation Error",
	                JOptionPane.ERROR_MESSAGE
	        );
	    }

    }
}