package eu.ksbrt.shortener.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.ksbrt.shortener.model.User;
import eu.ksbrt.shortener.service.UserService;

@RestController
public class UserController {
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index() {
        return "Hello World!";
    }

    @PostMapping
    public void addUser(User user) {
        userService.addUser(user);
    }
}
