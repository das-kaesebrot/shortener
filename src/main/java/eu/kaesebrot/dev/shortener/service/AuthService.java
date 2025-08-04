package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthRequestInitial;
import eu.kaesebrot.dev.shortener.model.AuthResponseBase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponseBase authenticate(AuthRequestInitial authRequestInitial) {
        final var token = UsernamePasswordAuthenticationToken.unauthenticated(authRequestInitial.getUsername(), authRequestInitial.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        String jwt = jwtService.generateToken(authentication);
        Long expiresAt = jwtService.extractExpirationTime(jwt);

        return new AuthResponseBase(jwt, authentication.getName(), expiresAt);
    }
}
