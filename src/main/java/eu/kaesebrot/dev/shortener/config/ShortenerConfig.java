package eu.kaesebrot.dev.shortener.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "shortener")
public class ShortenerConfig {
    private boolean userSignupEnabled = false;
    private boolean emailVerificationRequired = false;

    private String adminUsername = "admin";
    private String adminEmail = "mail@example.com";
    private String adminPassword = null;
}
