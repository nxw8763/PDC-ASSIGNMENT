package gui.admin;

import controller.CategoryController;
import model.users.Admin;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminCategoriesPanel
        extends JPanel
        implements CategoryManagementView {

    private final Admin admin;
    private final CategoryController controller;

    private final DefaultListModel<String> model =
            new DefaultListModel<>();

    private final JList<String> list =
            new JList<>(model);

    public AdminCategoriesPanel(
            Admin admin,
            CategoryController controller) {

        this.admin = admin;
        this.controller = controller;

        initialise();
    }

    private void initialise() {

        setLayout(
                new BorderLayout()
        );

        JButton addButton =
                new JButton("Add");

        JButton deleteButton =
                new JButton("Delete");

        JButton refreshButton =
                new JButton("Refresh");

        JPanel topPanel =
                new JPanel();

        topPanel.add(addButton);
        topPanel.add(deleteButton);
        topPanel.add(refreshButton);

        add(
                topPanel,
                BorderLayout.NORTH
        );

        add(
                new JScrollPane(list),
                BorderLayout.CENTER
        );

        addButton.addActionListener(
                e -> controller.addCategory(
                        admin,
                        this
                )
        );

        deleteButton.addActionListener(
                e -> controller.deleteCategory(
                        admin,
                        this
                )
        );

        refreshButton.addActionListener(
                e -> controller.loadCategories(
                        this
                )
        );

        controller.loadCategories(
                this
        );
    }

    @Override
    public void displayCategories(
            List<String> categories) {

        model.clear();

        for (String category : categories) {

            model.addElement(
                    category
            );
        }
    }

    @Override
    public String getSelectedCategory() {

        return list.getSelectedValue();
    }

    @Override
    public void showMessage(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message
        );
    }

    @Override
    public void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}