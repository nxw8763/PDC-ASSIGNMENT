package controller;

import dto.UserDTO;
import dto.UserFormDTO;
import gui.admin.users.CreateUserDialog;
import gui.admin.users.EditUserDialog;
import gui.admin.users.UserFormView;
import gui.admin.users.UserManagementView;
import model.users.Admin;
import model.users.User;
import service.UserService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService) {

        this.userService = userService;
    }

    /*
     * ==========================================
     * LOAD USERS
     * ==========================================
     */

    public void loadUsers(
            Admin admin,
            UserManagementView view) {

        List<UserDTO> dtoList =
                new ArrayList<>();

        for (User user :
                userService.getAllUsers(admin)) {

            dtoList.add(
                    new UserDTO(
                            user.getUserID(),
                            user.getUsername(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole()
                    )
            );
        }

        view.displayUsers(dtoList);
    }

    /*
     * ==========================================
     * CREATE USER
     * ==========================================
     */

    public void createUser(
            Admin admin,
            UserFormView view) {

        UserFormDTO dto =
                view.getFormData();

        try {

            userService.createUser(
                    admin,
                    dto.getName(),
                    dto.getUsername(),
                    dto.getPassword(),
                    dto.getRole(),
                    dto.getEmail()
            );

            view.close();

        } catch (IllegalArgumentException ex) {

            view.showError(
                    ex.getMessage()
            );
        }
    }
    
    public void openCreateUser(
            Admin admin,
            UserManagementView view) {

        CreateUserDialog dialog =
                new CreateUserDialog(
                        view.getParentWindow(),
                        admin,
                        this
                );

        dialog.setVisible(true);

        loadUsers(
                admin,
                view
        );
    }

    /*
     * ==========================================
     * EDIT USER
     * ==========================================
     */

    public void updateUser(
            Admin admin,
            int userId,
            UserFormView view) {

        UserFormDTO dto =
                view.getFormData();

        try {

            userService.updateUserField(
                    admin,
                    userId,
                    "username",
                    dto.getUsername()
            );

            userService.updateUserField(
                    admin,
                    userId,
                    "name",
                    dto.getName()
            );

            userService.updateUserField(
                    admin,
                    userId,
                    "email",
                    dto.getEmail()
            );

            userService.updateUserField(
                    admin,
                    userId,
                    "role",
                    dto.getRole()
            );

            if (!dto.getPassword().isBlank()) {

                userService.updateUserPassword(
                        admin,
                        userId,
                        dto.getPassword()
                );
            }

            view.close();

        } catch (IllegalArgumentException ex) {

            view.showError(
                    ex.getMessage()
            );
        }
    }
    
    public void openEditUser(
            Admin admin,
            UserDTO user,
            UserManagementView view) {

        EditUserDialog dialog =
                new EditUserDialog(
                        view.getParentWindow(),
                        admin,
                        user,
                        this
                );

        dialog.setVisible(true);

        loadUsers(
                admin,
                view
        );
    }

    /*
     * ==========================================
     * DELETE USER
     * ==========================================
     */

    public void deleteUser(
            Admin admin,
            int userId,
            UserManagementView view) {

        int result =
                JOptionPane.showConfirmDialog(
                        null,
                        "Delete user?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        userService.removeUser(
                admin,
                userId
        );

        loadUsers(
                admin,
                view
        );
    }
}