package eu.kaesebrot.dev.shortener.api;
import java.net.URI;
import java.util.UUID;

import eu.kaesebrot.dev.shortener.model.*;
import eu.kaesebrot.dev.shortener.model.dto.request.AuthRequestInitial;
import eu.kaesebrot.dev.shortener.model.dto.request.AuthRequestRefresh;
import eu.kaesebrot.dev.shortener.model.dto.request.AuthUserCreationRequest;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthResponseBase;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthResponseInitial;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthResponseRefresh;
import eu.kaesebrot.dev.shortener.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import eu.kaesebrot.dev.shortener.repository.AuthUserRepository;
import eu.kaesebrot.dev.shortener.service.EmailConfirmationTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("${shortener.hosting.subdirectory:}/api/v1/auth")
@Tag(name = "auth", description = "The auth API")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUserRepository userRepository;
    private final EmailConfirmationTokenService confirmationTokenService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("users")
    public ResponseEntity<AuthUser> registerUser(HttpServletRequest request, @Valid @RequestBody AuthUserCreationRequest authUserCreationRequest) {
        if (userRepository.existsByUsernameOrEmail(authUserCreationRequest.username(),  authUserCreationRequest.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists!");
        }

        AuthUser user = new AuthUser(authUserCreationRequest.username(), passwordEncoder.encode(authUserCreationRequest.username()), authUserCreationRequest.email());
        userRepository.save(user);
        
        confirmationTokenService.generateAndSendConfirmationTokenToUser(user, URI.create(request.getRequestURL().toString()), String.format("api/v1/shortener/users/%s/confirm", user.getId().toString()));

        return ResponseEntity.ok(user);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<AuthUser> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found")));
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity deleteUser(@PathVariable UUID id) {
        long deletedUsers = userRepository.removeById(id);
        if (deletedUsers <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users")
    public ResponseEntity<Page<AuthUser>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAll(pageable));
    }

    @GetMapping("users/me")
    public ResponseEntity<AuthUser> getSelf(final Authentication authentication) {
        AuthUser user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found"));

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("users/me")
    public ResponseEntity deleteSelf(final Authentication authentication) {
        long deletedUsers = userRepository.removeByUsername(authentication.getName());
        if (deletedUsers <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users/{id}/confirm/{token}")
    public ResponseEntity<AuthUser> confirmUserAccount(@PathVariable("id") UUID id, @PathVariable("token") String rawToken) {
        AuthUser user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found"));
        confirmationTokenService.redeemToken(user, rawToken);

        return ResponseEntity.ok(userRepository.findById(id).get());
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseInitial> getJwt(@Valid @RequestBody AuthRequestInitial request) {
        AuthResponseBase responseBase = authService.authenticate(request);

        // TODO
        String rawRefreshToken = "token";
        passwordEncoder.encode(rawRefreshToken);
        return ResponseEntity.ok(new AuthResponseInitial(responseBase, rawRefreshToken));
    }

    @PostMapping("refresh")
    public ResponseEntity<AuthResponseRefresh> refreshJwt(@Valid @RequestBody AuthRequestRefresh request) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("logout")
    public ResponseEntity logoutUser(final Authentication authentication) {
        // TODO
        return ResponseEntity.ok(null);
    }
}
