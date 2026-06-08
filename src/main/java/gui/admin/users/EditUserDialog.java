package gui.admin.users;

import controller.UserController;
import dto.UserDTO;
import dto.UserFormDTO;
import model.users.Admin;

import javax.swing.*;
import java.awt.*;

public class EditUserDialog
        extends JDialog
        implements UserFormView {

    private final Admin admin;

    private final int userId;

    private final UserController controller;

    private final UserFormPanel form =
            new UserFormPanel();

    public EditUserDialog(
            Window owner,
            Admin admin,
            UserDTO user,
            UserController controller) {

        super(
                owner,
                "Edit User",
                ModalityType.APPLICATION_MODAL
        );

        this.admin = admin;
        this.userId = user.getUserId();
        this.controller = controller;

        initialise(user);
    }

    private void initialise(
            UserDTO user) {

        form.setUsername(
                user.getUsername()
        );

        form.setName(
                user.getName()
        );

        form.setEmail(
                user.getEmail()
        );

        form.setRole(
                user.getRole()
        );

        setLayout(new BorderLayout());

        add(form, BorderLayout.CENTER);

        JButton saveButton =
                new JButton("Save");

        saveButton.addActionListener(
                e -> controller.updateUser(
                        admin,
                        userId,
                        this
                )
        );

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottom.add(saveButton);

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