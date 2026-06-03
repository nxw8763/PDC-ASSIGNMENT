package gui.admin;

import model.User;
import service.UserManagementService;

import javax.swing.*;
import java.awt.*;

public class UserEditorDialog extends JDialog {

    public UserEditorDialog(
            Window owner,
            User user,
            UserManagementService service
    ) {

        super(owner, "Edit User", ModalityType.APPLICATION_MODAL);

        setLayout(new GridLayout(5,2));

        JTextField name = new JTextField(user.getName());
        JTextField username = new JTextField(user.getUsername());
        JTextField email = new JTextField(user.getEmail());

        JComboBox<String> role = new JComboBox<>(
                new String[]{"1","2","3"}
        );

        JButton save = new JButton("Save");

        add(new JLabel("Name")); add(name);
        add(new JLabel("Username")); add(username);
        add(new JLabel("Email")); add(email);
        add(new JLabel("Role")); add(role);
        add(save);

        save.addActionListener(e -> {

            service.updateUserField(user.getUserID(), "name", name.getText());
            service.updateUserField(user.getUserID(), "username", username.getText());
            service.updateUserField(user.getUserID(), "email", email.getText());
            service.updateUserRole(user.getUserID(), (String) role.getSelectedItem());

            dispose();
        });

        pack();
        setLocationRelativeTo(owner);
    }
}