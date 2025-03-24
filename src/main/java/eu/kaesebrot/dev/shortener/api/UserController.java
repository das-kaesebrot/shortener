package eu.kaesebrot.dev.shortener.api;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.kaesebrot.dev.shortener.enums.UserState;
import eu.kaesebrot.dev.shortener.model.ShortenerUser;
import eu.kaesebrot.dev.shortener.model.UserCreation;
import eu.kaesebrot.dev.shortener.repository.ShortenerUserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/shortener/users")
@Tag(name = "users", description = "The User API")
public class UserController {
    private final ShortenerUserRepository _userRepository;
    private final BCryptPasswordEncoder _passwordEncoder = new BCryptPasswordEncoder();
    
    UserController(ShortenerUserRepository repository) {
        _userRepository = repository;
    }

    @PostMapping
    ShortenerUser registerUser(@RequestBody UserCreation userCreation) {

        if (_userRepository.existsByEmail(userCreation.getEmail()) || _userRepository.existsByUsername(userCreation.getUsername())) {
            throw new IllegalArgumentException("User already exists!");
        }

        ShortenerUser user = new ShortenerUser(userCreation.getUsername(), _passwordEncoder.encode(userCreation.getRawPassword()), userCreation.getEmail());
        user.addState(UserState.CONFIRMING_EMAIL);
        
        _userRepository.save(user);

        return user;
    }

    @GetMapping("{id}")
    ShortenerUser getUser(@PathVariable Long id) {
        return _userRepository.findById(id).orElseThrow();
    }
}
