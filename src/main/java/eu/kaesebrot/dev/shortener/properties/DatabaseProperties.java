package eu.kaesebrot.dev.shortener.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "shortener.db")
@Getter
@Setter
public class DatabaseProperties {
    @Getter(AccessLevel.NONE)
    private final String DEFAULT_URL = "jdbc:sqlite:shortener.db";

    @Getter(AccessLevel.NONE)
    private final String DEFAULT_DRIVER_CLASS_NAME = "org.sqlite.JDBC";

    @Getter(AccessLevel.NONE)
    private final String DEFAULT_DATABASE_PLATFORM = "org.hibernate.community.dialect.SQLiteDialect";

    private String url = DEFAULT_URL;
    private String driverClassName = DEFAULT_DRIVER_CLASS_NAME;
    private String databasePlatform = DEFAULT_DATABASE_PLATFORM;
    private String username;
    private String password;
}
