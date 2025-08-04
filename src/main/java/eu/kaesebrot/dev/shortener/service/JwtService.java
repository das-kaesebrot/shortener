package eu.kaesebrot.dev.shortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;

@RequiredArgsConstructor
public class JwtService {
    private final String issuer;
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final Duration tokenLifeTime;

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Instant now = Instant.now();

        Collection<String> authorityStrings = authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.replaceFirst("^SCOPE_", ""))
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(tokenLifeTime))
                .subject(username)
                .claim("scope", authorityStrings)
                .build();
        var encoderParameters = JwtEncoderParameters.from(claims);
        return encoder.encode(encoderParameters).getTokenValue();
    }

    public Long extractExpirationTime(String token) {
        Jwt jwt = decoder.decode(token);
        var exp = (Instant) jwt.getClaim("exp");
        return exp.toEpochMilli();
    }
}
