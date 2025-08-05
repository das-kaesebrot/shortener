package eu.kaesebrot.dev.shortener.api;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

import eu.kaesebrot.dev.shortener.model.*;
import eu.kaesebrot.dev.shortener.model.dto.request.AuthRequestInitial;
import eu.kaesebrot.dev.shortener.model.dto.request.AuthRequestRefresh;
import eu.kaesebrot.dev.shortener.model.dto.request.AuthUserRequestCreation;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthResponseInitial;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthResponseRefresh;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthUserResponse;
import eu.kaesebrot.dev.shortener.service.AuthService;
import eu.kaesebrot.dev.shortener.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
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
@SecurityScheme(
        name = "Authorization",
        scheme = "Bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
@RequiredArgsConstructor
public class AuthController {
    private final AuthUserRepository userRepository;
    private final EmailConfirmationTokenService confirmationTokenService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RefreshTokenService refreshTokenService;

    @PostMapping("users")
    @Transactional
    public ResponseEntity<AuthUserResponse> registerUser(HttpServletRequest request, @Valid @RequestBody AuthUserRequestCreation authUserRequestCreation) {
        if (userRepository.existsByUsernameOrEmail(authUserRequestCreation.username(),  authUserRequestCreation.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists!");
        }

        AuthUser user = new AuthUser(authUserRequestCreation.username(), passwordEncoder.encode(authUserRequestCreation.username()), authUserRequestCreation.email(), Set.of(new SimpleGrantedAuthority("SCOPE_self")));
        userRepository.save(user);
        
        confirmationTokenService.generateAndSendConfirmationTokenToUser(user, URI.create(request.getRequestURL().toString()), String.format("api/v1/shortener/users/%s/confirm", user.getId().toString()));

        return ResponseEntity.ok(user.toDto());
    }

    @GetMapping("users/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<AuthUserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found")).toDto());
    }

    @DeleteMapping("users/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity deleteUser(@PathVariable UUID id) {
        long deletedUsers = userRepository.removeById(id);
        if (deletedUsers <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Page<AuthUserResponse>> getUsers(@Valid @RequestParam(defaultValue = "0") @Min(0) int page, @Valid @RequestParam(defaultValue = "50") @Min(0) @Max(50) int size) {
        return ResponseEntity.ok(userRepository.findAll(Pageable.ofSize(size).withPage(page)).map(AuthUser::toDto));
    }

    @GetMapping("users/me")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<AuthUserResponse> getSelf(final Authentication authentication) {
        AuthUser user = userRepository.findById(UUID.fromString(authentication.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found"));

        return ResponseEntity.ok(user.toDto());
    }

    @DeleteMapping("users/me")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity deleteSelf(final Authentication authentication) {
        long deletedUsers = userRepository.removeById(UUID.fromString(authentication.getName()));
        if (deletedUsers <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users/{id}/confirm/{token}")
    public ResponseEntity<AuthUserResponse> confirmUserAccount(@PathVariable("id") UUID id, @PathVariable("token") String rawToken) {
        AuthUser user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The user could not be found"));
        confirmationTokenService.redeemToken(user, rawToken);

        return ResponseEntity.ok(userRepository.findById(id).get().toDto());
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseInitial> getJwt(@Valid @RequestBody AuthRequestInitial request) {
        return ResponseEntity.ok(authService.authenticateViaUsernameAndPassword(request));
    }

    @PostMapping("refresh")
    public ResponseEntity<AuthResponseRefresh> refreshJwt(@Valid @RequestBody AuthRequestRefresh request) {
        return ResponseEntity.ok(authService.authenticateViaUserIdAndRefreshToken(request));
    }

    @PostMapping("logout")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity revokeSingleRefreshToken(final Authentication authentication, @Valid @RequestBody String refreshToken) {
        refreshTokenService.deleteRefreshTokenByRawToken(UUID.fromString(authentication.getName()), refreshToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("revoke")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity revokeAllRefreshTokens(final Authentication authentication) {
        refreshTokenService.deleteAllRefreshTokensOfUser(UUID.fromString(authentication.getName()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("debug")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity debugToken(final Authentication authentication, final JwtAuthenticationToken auth) {
        return ResponseEntity.noContent().build();
    }
}
