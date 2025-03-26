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
import eu.kaesebrot.dev.shortener.service.EmailConfirmationTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/shortener/users")
@Tag(name = "users", description = "The User API")
public class UserController {
    private final ShortenerUserRepository userRepository;
    private final EmailConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    UserController(ShortenerUserRepository userRepository, EmailConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
    }

    @PostMapping
    ShortenerUser registerUser(@Valid @RequestBody UserCreation userCreation) {

        if (userRepository.existsByEmail(userCreation.getEmail()) || userRepository.existsByUsername(userCreation.getUsername())) {
            throw new IllegalArgumentException("User already exists!");
        }

        ShortenerUser user = new ShortenerUser(userCreation.getUsername(), passwordEncoder.encode(userCreation.getRawPassword()), userCreation.getEmail());
        // TODO send out token link via mail
        String rawToken = confirmationTokenService.generateConfirmationTokenForUser(user);
        user.updateHashedConfirmationToken(passwordEncoder.encode(rawToken));
        userRepository.save(user);

        return user;
    }

    @GetMapping("{id}")
    ShortenerUser getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @GetMapping("confirm/{rawToken}")
    void confirmUserAccount(@PathVariable String rawToken) {
        confirmationTokenService.redeemToken(rawToken);
    }
}
