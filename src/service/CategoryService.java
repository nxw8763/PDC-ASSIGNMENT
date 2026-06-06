package service;

import dao.CategoryDAO;
import model.Admin;
import model.User;
import model.enums.AuditAction;
import model.enums.AuditEntity;

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
        	AuditService.addAuditLog(currentUser.getUserID(), AuditAction.VIOLATION, AuditEntity.USER , currentUser.getUserID(), currentUser.getUsername() +  " violated access policy for categorys");
            throw new SecurityException(
                    "Only administrators can add categories.");
        }
        AuditService.addAuditLog(currentUser.getUserID(), AuditAction.CREATE, AuditEntity.CATEGORY, currentUser.getUserID(), currentUser.getUsername() + " created category " + category);
        categoryDAO.addCategory(category);
    }

    public void deleteCategory(User currentUser, String category) {

        if (!(currentUser instanceof Admin)) {
        	AuditService.addAuditLog(currentUser.getUserID(), AuditAction.VIOLATION, AuditEntity.USER , currentUser.getUserID(), currentUser.getUsername() +  " violated access policy for categorys");
            throw new SecurityException(
                    "Only administrators can delete categories.");
        }
        AuditService.addAuditLog(currentUser.getUserID(), AuditAction.DELETE, AuditEntity.CATEGORY, currentUser.getUserID(), currentUser.getUsername() + " deleated category " + category);
        categoryDAO.deleteCategory(category);
    }
}