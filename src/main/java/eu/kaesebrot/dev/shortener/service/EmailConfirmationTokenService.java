package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.ShortenerUser;

public interface EmailConfirmationTokenService {
    String generateConfirmationTokenForUser(ShortenerUser user);
    void redeemToken(String rawToken);
}
