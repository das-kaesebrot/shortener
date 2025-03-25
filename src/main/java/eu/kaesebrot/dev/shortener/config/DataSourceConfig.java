package eu.kaesebrot.dev.shortener.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.kaesebrot.dev.shortener.properties.DatabaseProperties;
import eu.kaesebrot.dev.shortener.utils.StringUtils;

@Configuration
public class DataSourceConfig {
    private final DatabaseProperties _databaseProperties;

    public DataSourceConfig(DatabaseProperties databaseProperties) {
        _databaseProperties = databaseProperties;
    }

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create()
                .driverClassName(_databaseProperties.getDriverClassName())
                .url(_databaseProperties.getUrl());

        if (!StringUtils.isNullOrEmpty(_databaseProperties.getUsername())
                && !StringUtils.isNullOrEmpty(_databaseProperties.getPassword())) {
            dataSourceBuilder
                    .username(_databaseProperties.getUsername())
                    .password(_databaseProperties.getPassword());
        }

        return dataSourceBuilder.build();
    }
}