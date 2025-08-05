package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.config.JwtConfig;
import eu.kaesebrot.dev.shortener.model.AuthUser;
import eu.kaesebrot.dev.shortener.model.dto.request.AuthRequestInitial;
import eu.kaesebrot.dev.shortener.model.dto.request.AuthRequestRefresh;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthResponseInitial;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthResponseRefresh;
import eu.kaesebrot.dev.shortener.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final JwtConfig jwtConfig;
    private final AuthUserRepository authUserRepository;

    public AuthResponseInitial authenticateViaUsernameAndPassword(AuthRequestInitial authRequestInitial) {
        final var token = UsernamePasswordAuthenticationToken.unauthenticated(authRequestInitial.username(), authRequestInitial.password());
        Authentication authentication = authenticationManager.authenticate(token);

        final AuthUser user = authUserRepository.findByUsername(authentication.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not found!"));

        String jwt = jwtService.generateToken(user.getId(), authentication.getAuthorities());
        Long expiresAt = jwtService.extractExpirationTime(jwt);
        Instant refreshTokenExpiresAt = Instant.now().plus(jwtConfig.getRefreshTokenTtl());
        String rawRefreshToken = refreshTokenService.generateRefreshTokenForUser(user, refreshTokenExpiresAt);

        return new AuthResponseInitial(jwt, user.getId(), expiresAt, rawRefreshToken, refreshTokenExpiresAt.toEpochMilli());
    }

    public AuthResponseRefresh authenticateViaUserIdAndRefreshToken(AuthRequestRefresh authRequestRefresh) {
        if (!refreshTokenService.isRefreshTokenValid(authRequestRefresh.refreshToken(), authRequestRefresh.userId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh token!");
        }

        final AuthUser user = authUserRepository.findById(authRequestRefresh.userId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not found!"));

        String jwt = jwtService.generateToken(user.getId(), user.getAuthorities());
        Long expiresAt = jwtService.extractExpirationTime(jwt);

        return new AuthResponseRefresh(jwt, user.getId(), expiresAt);
    }
}
