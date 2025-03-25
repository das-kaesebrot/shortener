package eu.kaesebrot.dev.shortener.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("shortener")
public class ShortenerProperties {
    
    @NestedConfigurationProperty
    private MailProperties mailProperties;
}
