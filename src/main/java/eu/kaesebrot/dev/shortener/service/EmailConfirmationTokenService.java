package eu.kaesebrot.dev.shortener.service;

import java.net.URI;

import eu.kaesebrot.dev.shortener.model.AuthUser;

public interface EmailConfirmationTokenService {
    void generateAndSendConfirmationTokenToUser(AuthUser user, URI originalRequestUri, String tokenConfirmationPath);
    void redeemToken(AuthUser user, String rawToken);
}
