package eu.kaesebrot.dev.shortener.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "shortener.db")
public class DatabaseProperties {

    private final String DEFAULT_URL = "jdbc:sqlite:shortener.db";
    private final String DEFAULT_DRIVER_CLASS_NAME = "org.sqlite.JDBC";
    private final String DEFAULT_DATABASE_PLATFORM = "org.hibernate.community.dialect.SQLiteDialect";

    private String url = DEFAULT_URL;
    private String driverClassName = DEFAULT_DRIVER_CLASS_NAME;
    private String databasePlatform = DEFAULT_DATABASE_PLATFORM;
    private String username;
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDatabasePlatform() {
        return databasePlatform;
    }

    public void setDatabasePlatform(String databasePlatform) {
        this.databasePlatform = databasePlatform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
