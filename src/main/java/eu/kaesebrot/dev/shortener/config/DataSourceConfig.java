package eu.kaesebrot.dev.shortener.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.kaesebrot.dev.shortener.properties.DatabaseProperties;
import eu.kaesebrot.dev.shortener.utils.StringUtils;

@Configuration
public class DataSourceConfig {
    private final DatabaseProperties databaseProperties;

    public DataSourceConfig(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create()
                .driverClassName(databaseProperties.getDriverClassName())
                .url(databaseProperties.getUrl());

        if (!StringUtils.isNullOrEmpty(databaseProperties.getUsername())
                && !StringUtils.isNullOrEmpty(databaseProperties.getPassword())) {
            dataSourceBuilder
                    .username(databaseProperties.getUsername())
                    .password(databaseProperties.getPassword());
        }

        return dataSourceBuilder.build();
    }
}