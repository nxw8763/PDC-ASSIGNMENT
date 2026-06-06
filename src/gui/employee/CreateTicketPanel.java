package gui.employee;

import model.Employee;
import model.Ticket;
import model.enums.Priority;
import service.CategoryService;
import service.TicketManagementService;

import javax.swing.*;
import java.awt.*;

public class CreateTicketPanel extends JPanel {

    private final Employee employee;
    private final TicketManagementService ticketService;
    private final CategoryService categoryService;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> categoryBox;
    private JComboBox<Priority> priorityBox;

    public CreateTicketPanel(Employee employee,
                             TicketManagementService ticketService,
                             CategoryService categoryService) {

        this.employee = employee;
        this.ticketService = ticketService;
        this.categoryService = categoryService;

        initialise();
    }

    private void initialise() {

        setLayout(new BorderLayout());

        JPanel form = new JPanel(
                new GridBagLayout()
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        titleField = new JTextField();

        descriptionArea =
                new JTextArea(6,20);

        categoryBox =
                new JComboBox<>();

        categoryService
                .getCategories()
                .forEach(categoryBox::addItem);

        priorityBox =
                new JComboBox<>(Priority.values());

        addField(form, gbc, 0,
                "Title", titleField);

        addField(form, gbc, 1,
                "Category", categoryBox);

        addField(form, gbc, 2,
                "Priority", priorityBox);

        gbc.gridx = 0;
        gbc.gridy = 3;

        form.add(
                new JLabel("Description"),
                gbc
        );

        gbc.gridx = 1;

        form.add(
                new JScrollPane(descriptionArea),
                gbc
        );

        JButton createButton =
                new JButton("Create Ticket");

        createButton.addActionListener(
                e -> createTicket()
        );

        gbc.gridx = 1;
        gbc.gridy = 4;

        form.add(createButton, gbc);

        add(form, BorderLayout.NORTH);
    }

    private void createTicket() {

        String title =
                titleField.getText().trim();

        String description =
                descriptionArea.getText().trim();

        if (title.isBlank()
                || description.isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "All fields are required."
            );

            return;
        }

        Ticket ticket =
                ticketService.createTicket(
                        title,
                        description,
                        (String) categoryBox.getSelectedItem(),
                        (Priority) priorityBox.getSelectedItem(),
                        employee
                );

        JOptionPane.showMessageDialog(
                this,
                "Ticket Created. ID: "
                        + ticket.getTicketID()
        );

        titleField.setText("");
        descriptionArea.setText("");
    }

    private void addField(JPanel panel,
                          GridBagConstraints gbc,
                          int row,
                          String label,
                          Component component) {

        gbc.gridx = 0;
        gbc.gridy = row;

        panel.add(
                new JLabel(label),
                gbc
        );

        gbc.gridx = 1;

        panel.add(component, gbc);
    }
}