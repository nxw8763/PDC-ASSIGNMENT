package service;

import dao.CategoryDAO;
import model.Admin;
import model.User;

import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<String> getCategories() {
        return categoryDAO.getCategories();
    }

    public void addCategory(User currentUser, String category) {

        if (!(currentUser instanceof Admin)) {
            throw new SecurityException(
                    "Only administrators can add categories.");
        }

        categoryDAO.addCategory(category);
    }

    public void deleteCategory(User currentUser, String category) {

        if (!(currentUser instanceof Admin)) {
            throw new SecurityException(
                    "Only administrators can delete categories.");
        }

        categoryDAO.deleteCategory(category);
    }
}