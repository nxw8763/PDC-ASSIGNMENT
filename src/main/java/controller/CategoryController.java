package controller;

import gui.admin.CategoryManagementView;
import gui.employee.CategorySelectionView;
import model.users.Admin;
import service.CategoryService;

import javax.swing.*;
import java.util.List;

public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    /*
     * ============================================
     * LOAD
     * ============================================
     */

    public void loadCategories(
            CategoryManagementView view) {

        List<String> categories =
                categoryService.getCategories();

        view.displayCategories(
                categories
        );
    }
    
    public void loadCategories(
            CategorySelectionView view) {

        view.displayCategories(
                categoryService.getCategories()
        );
    }

    /*
     * ============================================
     * ADD
     * ============================================
     */

    public void addCategory(
            Admin admin,
            CategoryManagementView view) {

        String category =
                JOptionPane.showInputDialog(
                        null,
                        "Category"
                );

        if (category == null
                || category.isBlank()) {

            return;
        }

        try {

            categoryService.addCategory(
                    admin,
                    category
            );

            loadCategories(view);

            view.showMessage(
                    "Category added."
            );

        } catch (IllegalArgumentException ex) {

            view.showError(
                    ex.getMessage()
            );
        }
    }

    /*
     * ============================================
     * DELETE
     * ============================================
     */

    public void deleteCategory(
            Admin admin,
            CategoryManagementView view) {

        String category =
                view.getSelectedCategory();

        if (category == null) {

            view.showError(
                    "Select a category."
            );

            return;
        }

        try {

            categoryService.deleteCategory(
                    admin,
                    category
            );

            loadCategories(view);

            view.showMessage(
                    "Category deleted."
            );

        } catch (IllegalArgumentException ex) {

            view.showError(
                    ex.getMessage()
            );
        }
    }
}