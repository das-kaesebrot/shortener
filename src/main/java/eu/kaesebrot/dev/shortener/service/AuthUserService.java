package eu.kaesebrot.dev.shortener.service;

import eu.kaesebrot.dev.shortener.model.AuthUser;

public interface AuthUserService {
    String createNewUserAndReturnGeneratedPassword(String username, String email);
    AuthUser createNewUser(String username, String email, String rawPassword);
}
