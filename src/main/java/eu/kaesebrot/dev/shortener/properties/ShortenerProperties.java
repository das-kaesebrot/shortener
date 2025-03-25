package eu.kaesebrot.dev.shortener.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("shortener")
public class ShortenerProperties {

    @NestedConfigurationProperty
    private MailProperties mail;

    @NestedConfigurationProperty
    private DatabaseProperties db;

    public MailProperties getMailProperties() {
        return mail;
    }

    public DatabaseProperties getDatabaseProperties() {
        return db;
    }
}
