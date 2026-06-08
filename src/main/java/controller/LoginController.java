package controller;

import model.users.User;
import service.UserService;

public class LoginController {

    private final UserService userService;

    public LoginController(
            UserService userService
    ) {
        this.userService = userService;
    }

    public User login(
            String username,
            String password
    ) {

        User user = userService.authenticate(
                username,
                password
        );
        
        if(user == null) {
        	throw new SecurityException("Access Denied!");
        }
        return user;
    }
}