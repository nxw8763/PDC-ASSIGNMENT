package gui.admin.users;

import dto.UserDTO;

import java.awt.Window;
import java.util.List;

public interface UserManagementView {

    void displayUsers(
            List<UserDTO> users
    );

    Window getParentWindow();

    void showMessage(
            String message
    );

    void showError(
            String message
    );
}