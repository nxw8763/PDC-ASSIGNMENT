package gui.admin;

import java.util.List;

public interface CategoryManagementView {

    void displayCategories(
            List<String> categories
    );

    void showMessage(
            String message
    );

    void showError(
            String message
    );

    String getSelectedCategory();
}