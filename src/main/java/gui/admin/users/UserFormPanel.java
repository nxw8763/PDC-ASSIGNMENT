package gui.admin.users;

import javax.swing.*;
import java.awt.*;

public class UserFormPanel extends JPanel {

    private final JTextField usernameField =
            new JTextField(20);

    private final JTextField nameField =
            new JTextField(20);

    private final JTextField emailField =
            new JTextField(20);

    private final JPasswordField passwordField =
            new JPasswordField(20);

    private final JComboBox<String> roleBox =
            new JComboBox<>(
                    new String[] {
                            "EMPLOYEE",
                            "TECHNICIAN",
                            "ADMINISTRATOR"
                    }
            );

    public UserFormPanel() {

        setLayout(
                new GridBagLayout()
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        5,
                        5,
                        5,
                        5
                );

        gbc.anchor =
                GridBagConstraints.WEST;

        addRow(
                gbc,
                0,
                "Username",
                usernameField
        );

        addRow(
                gbc,
                1,
                "Name",
                nameField
        );

        addRow(
                gbc,
                2,
                "Email",
                emailField
        );

        addRow(
                gbc,
                3,
                "Password",
                passwordField
        );

        addRow(
                gbc,
                4,
                "Role",
                roleBox
        );
    }

    private void addRow(
            GridBagConstraints gbc,
            int row,
            String label,
            Component component) {

        gbc.gridx = 0;
        gbc.gridy = row;

        add(
                new JLabel(
                        label + ":"
                ),
                gbc
        );

        gbc.gridx = 1;

        add(
                component,
                gbc
        );
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getName() {
        return nameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(
                passwordField.getPassword()
        );
    }

    public String getRole() {
        return (String)
                roleBox.getSelectedItem();
    }

    public void setUsername(
            String username) {

        usernameField.setText(
                username
        );
    }

    public void setName(
            String name) {

        nameField.setText(
                name
        );
    }

    public void setEmail(
            String email) {

        emailField.setText(
                email
        );
    }

    public void setRole(
            String role) {

        roleBox.setSelectedItem(
                role
        );
    }
}