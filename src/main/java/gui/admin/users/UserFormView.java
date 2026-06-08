package gui.admin.users;

import dto.UserFormDTO;

public interface UserFormView {

    UserFormDTO getFormData();

    void close();

    void showError(
            String message
    );
}