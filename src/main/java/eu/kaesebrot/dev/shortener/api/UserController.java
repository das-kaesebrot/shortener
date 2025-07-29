package eu.kaesebrot.dev.shortener.api;
import java.net.URI;
import java.util.UUID;

import eu.kaesebrot.dev.shortener.model.AuthRequest;
import eu.kaesebrot.dev.shortener.model.AuthResponse;
import eu.kaesebrot.dev.shortener.service.AuthService;
import eu.kaesebrot.dev.shortener.service.AuthUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.kaesebrot.dev.shortener.model.ShortenerUser;
import eu.kaesebrot.dev.shortener.model.UserCreation;
import eu.kaesebrot.dev.shortener.repository.ShortenerUserRepository;
import eu.kaesebrot.dev.shortener.service.EmailConfirmationTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "auth", description = "The auth API")
public class UserController {
    private final ShortenerUserRepository userRepository;
    private final EmailConfirmationTokenService confirmationTokenService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    UserController(ShortenerUserRepository userRepository, EmailConfirmationTokenService confirmationTokenService, AuthService authService, AuthUserDetailsService authUserDetailsService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("users")
    ShortenerUser registerUser(HttpServletRequest request, @Valid @RequestBody UserCreation userCreation) {

        if (userRepository.existsByEmail(userCreation.getEmail()) || userRepository.existsByUsername(userCreation.getUsername())) {
            throw new IllegalArgumentException("User already exists!");
        }

        ShortenerUser user = new ShortenerUser(userCreation.getUsername(), passwordEncoder.encode(userCreation.getRawPassword()), userCreation.getEmail());
        userRepository.save(user);
        
        confirmationTokenService.generateAndSendConfirmationTokenToUser(user, URI.create(request.getRequestURL().toString()), String.format("api/v1/shortener/users/%s/confirm", user.getId().toString()));

        return user;
    }

    @GetMapping("users/{id}")
    ShortenerUser getUser(@PathVariable UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    @GetMapping("users")
    Page<ShortenerUser> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @GetMapping("users/{id}/confirm/{token}")
    ShortenerUser confirmUserAccount(@PathVariable("id") UUID id, @PathVariable("token") String rawToken) {
        ShortenerUser user = userRepository.findById(id).orElseThrow();
        confirmationTokenService.redeemToken(user, rawToken);

        return userRepository.findById(id).orElseThrow();
    }

    @PostMapping("login")
    AuthResponse loginUserAccount(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }
}
