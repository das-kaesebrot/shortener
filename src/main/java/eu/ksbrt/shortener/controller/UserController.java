package eu.ksbrt.shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import eu.ksbrt.shortener.model.User;
import eu.ksbrt.shortener.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void addUser(User user) {
        userService.addUser(user);
    }
}
