package eu.kaesebrot.dev.shortener.service;

import java.net.URI;

import eu.kaesebrot.dev.shortener.model.ShortenerUser;

public interface EmailConfirmationTokenService {
    void generateAndSendConfirmationTokenToUser(ShortenerUser user, URI originalRequestUri, String tokenConfirmationPath);
    void redeemToken(ShortenerUser user, String rawToken);
}
