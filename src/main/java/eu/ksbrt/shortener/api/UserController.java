package eu.ksbrt.shortener.api;

import org.springframework.beans.factory.annotation.Autowired;

import eu.ksbrt.shortener.model.User;
import eu.ksbrt.shortener.service.UserService;

public class UserController {
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void addUser(User user) {
        userService.addUser(user);
    }
}
