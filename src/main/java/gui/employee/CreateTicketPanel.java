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

    public CreateTicketPanel(
            Employee employee,
            TicketManagementService ticketService,
            CategoryService categoryService) {

        this.employee = employee;
        this.ticketService = ticketService;
        this.categoryService = categoryService;

        initialise();
    }

    private void initialise() {

        setLayout(new BorderLayout());

        // =========================
        // Header
        // =========================

        JLabel header =
                new JLabel("Create Support Ticket");

        header.setFont(
                header.getFont()
                        .deriveFont(
                                Font.BOLD,
                                24f
                        )
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        add(header, BorderLayout.NORTH);

        // =========================
        // Form Card
        // =========================

        JPanel form = new JPanel(
                new GridBagLayout()
        );

        form.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "New Ticket"
                        ),
                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        10,
                        10,
                        10,
                        10
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        titleField = new JTextField();

        titleField.setPreferredSize(
                new Dimension(
                        400,
                        30
                )
        );

        categoryBox =
                new JComboBox<>();

        categoryService
                .getCategories()
                .forEach(categoryBox::addItem);

        categoryBox.setPreferredSize(
                new Dimension(
                        400,
                        30
                )
        );

        priorityBox =
                new JComboBox<>(
                        Priority.values()
                );

        priorityBox.setPreferredSize(
                new Dimension(
                        400,
                        30
                )
        );

        descriptionArea =
                new JTextArea(
                        10,
                        40
                );

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScroll =
                new JScrollPane(
                        descriptionArea
                );

        descriptionScroll.setPreferredSize(
                new Dimension(
                        400,
                        200
                )
        );

        addField(
                form,
                gbc,
                0,
                "Title",
                titleField
        );

        addField(
                form,
                gbc,
                1,
                "Category",
                categoryBox
        );

        addField(
                form,
                gbc,
                2,
                "Priority",
                priorityBox
        );

        gbc.gridx = 0;
        gbc.gridy = 3;

        form.add(
                new JLabel(
                        "Description"
                ),
                gbc
        );

        gbc.gridx = 1;

        form.add(
                descriptionScroll,
                gbc
        );

        JButton createButton =
                new JButton(
                        "Create Ticket"
                );

        createButton.setPreferredSize(
                new Dimension(
                        200,
                        40
                )
        );

        createButton.addActionListener(
                e -> createTicket()
        );

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor =
                GridBagConstraints.CENTER;

        form.add(
                createButton,
                gbc
        );

        JPanel centerPanel =
                new JPanel(
                        new GridBagLayout()
                );

        centerPanel.add(form);

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        // =========================
        // Information Panel
        // =========================

        JPanel infoPanel =
                new JPanel();

        infoPanel.setLayout(
                new BoxLayout(
                        infoPanel,
                        BoxLayout.Y_AXIS
                )
        );

        infoPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Ticket Information"
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        infoPanel.add(
                new JLabel(
                        "Created By:"
                )
        );

        infoPanel.add(
                new JLabel(
                        employee.getUsername()
                )
        );

        infoPanel.add(
                Box.createVerticalStrut(
                        15
                )
        );

        infoPanel.add(
                new JLabel(
                        "Initial Status:"
                )
        );

        infoPanel.add(
                new JLabel(
                        "OPEN"
                )
        );

        infoPanel.add(
                Box.createVerticalStrut(
                        20
                )
        );

        infoPanel.add(
                new JLabel(
                        "Priority Guide"
                )
        );

        infoPanel.add(
                Box.createVerticalStrut(
                        10
                )
        );

        infoPanel.add(
                new JLabel(
                        "LOW - Minor issue"
                )
        );

        infoPanel.add(
                new JLabel(
                        "MEDIUM - Affects work"
                )
        );

        infoPanel.add(
                new JLabel(
                        "HIGH - Major issue"
                )
        );

        add(
                infoPanel,
                BorderLayout.EAST
        );
    }

    private void createTicket() {

        String title =
                titleField.getText()
                        .trim();

        String description =
                descriptionArea.getText()
                        .trim();

        if (title.isBlank()
                || description.isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "All fields are required."
            );

            return;
        }

        try {
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
        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        

        titleField.setText("");
        descriptionArea.setText("");
    }

    private void addField(
            JPanel panel,
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

        panel.add(
                component,
                gbc
        );
    }
}