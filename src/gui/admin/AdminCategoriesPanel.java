package gui.admin;

import service.CategoryService;

import javax.swing.*;
import java.awt.*;

public class AdminCategoriesPanel extends JPanel {

    private final CategoryService service;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);

    public AdminCategoriesPanel(CategoryService service) {

        this.service = service;

        setLayout(new BorderLayout());

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");

        JPanel top = new JPanel();
        top.add(add);
        top.add(delete);
        top.add(refresh);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        add.addActionListener(e -> addCategory());
        delete.addActionListener(e -> deleteCategory());
        refresh.addActionListener(e -> load());

        load();
    }

    private void load() {
        model.clear();
        service.getCategories().forEach(model::addElement);
    }

    private void addCategory() {

        String c = JOptionPane.showInputDialog(this, "Category");
        if (c == null) return;

        service.addCategory(c);
        load();
    }

    private void deleteCategory() {

        String selected = list.getSelectedValue();
        if (selected == null) return;

        service.deleteCategory(selected);
        load();
    }
}