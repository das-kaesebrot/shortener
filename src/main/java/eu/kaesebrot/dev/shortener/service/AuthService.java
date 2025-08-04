package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.config.JwtConfig;
import eu.kaesebrot.dev.shortener.model.dto.request.AuthRequestInitial;
import eu.kaesebrot.dev.shortener.model.dto.response.AuthResponseInitial;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final JwtConfig jwtConfig;

    public AuthResponseInitial authenticate(AuthRequestInitial authRequestInitial) {
        final var token = UsernamePasswordAuthenticationToken.unauthenticated(authRequestInitial.username(), authRequestInitial.password());
        Authentication authentication = authenticationManager.authenticate(token);

        String jwt = jwtService.generateToken(authentication);
        Long expiresAt = jwtService.extractExpirationTime(jwt);
        Instant refreshTokenExpiresAt = Instant.now().plus(jwtConfig.getRefreshTokenTtl());
        String rawRefreshToken = refreshTokenService.generateRefreshTokenForUser(authentication.getName(), refreshTokenExpiresAt);

        return new AuthResponseInitial(jwt, authentication.getName(), expiresAt, rawRefreshToken, refreshTokenExpiresAt.toEpochMilli());
    }
}
