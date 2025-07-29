package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthRequestInitial;
import eu.kaesebrot.dev.shortener.model.AuthResponseBase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponseBase authenticate(AuthRequestInitial authRequestInitial) {
        var token = new UsernamePasswordAuthenticationToken(authRequestInitial.getUsername(), authRequestInitial.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        String jwt = jwtService.generateToken(authentication);
        Long expiresAt = jwtService.extractExpirationTime(jwt);

        return new AuthResponseBase(jwt, authentication.getName(), expiresAt);
    }
}
