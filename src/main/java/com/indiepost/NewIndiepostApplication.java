package com.indiepost;

import com.indiepost.security.MySQLPasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableConfigurationProperties
public class NewIndiepostApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(NewIndiepostApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NewIndiepostApplication.class);
    }

    @Configuration
    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
    @EnableScheduling
    static class SchedulingConfiguration {

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MySQLPasswordEncoder();
    }
}