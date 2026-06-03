package service;

import dao.CategoryDAO;
import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<String> getCategories() {
        return categoryDAO.getCategories();
    }

    public void addCategory(String category) {
        categoryDAO.addCategory(category);
    }

    public void deleteCategory(String category) {
        categoryDAO.deleteCategory(category);
    }
}