package gui.admin.users;

import controller.UserController;
import dto.UserFormDTO;
import model.users.Admin;

import javax.swing.*;
import java.awt.*;

public class CreateUserDialog
        extends JDialog
        implements UserFormView {

    private final Admin admin;
    private final UserController controller;

    private final UserFormPanel form =
            new UserFormPanel();

    public CreateUserDialog(
            Window owner,
            Admin admin,
            UserController controller) {

        super(
                owner,
                "Create User",
                ModalityType.APPLICATION_MODAL
        );

        this.admin = admin;
        this.controller = controller;

        initialise();
    }

    private void initialise() {

        setLayout(new BorderLayout());

        add(form, BorderLayout.CENTER);

        JButton createButton =
                new JButton("Create User");

        createButton.addActionListener(
                e -> controller.createUser(
                        admin,
                        this
                )
        );

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottom.add(createButton);

        add(bottom, BorderLayout.SOUTH);

        pack();

        setLocationRelativeTo(getOwner());
    }

    @Override
    public UserFormDTO getFormData() {

        return new UserFormDTO(
                form.getUsername(),
                form.getName(),
                form.getEmail(),
                form.getPassword(),
                form.getRole()
        );
    }

    @Override
    public void close() {

        dispose();
    }

    @Override
    public void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}