package com.example.api.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfig {


    @Bean
    public Flyway flyway() {
        var flyway = Flyway.configure()
                .locations("classpath:db/migration")
                .dataSource("jdbc:h2:mem:testdb", "admin", "admin")
                .load();
        flyway.migrate();
        return flyway;
    }
}
