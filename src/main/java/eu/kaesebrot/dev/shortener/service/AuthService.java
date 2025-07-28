package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthRequest;
import eu.kaesebrot.dev.shortener.model.AuthResponse;
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

    public AuthResponse authenticate(AuthRequest authRequest) {
        var token = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        String jwt = jwtService.generateToken(authentication);
        Long expiresAt = jwtService.extractExpirationTime(jwt);

        return new AuthResponse(jwt, authentication.getName(), expiresAt);
    }
}
