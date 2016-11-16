package com.indiepost;

import com.indiepost.config.ImageConfig;
import com.indiepost.security.MySQLPasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;


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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MySQLPasswordEncoder();
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    @Bean
    public ImageConfig imageConfig() {
        return new ImageConfig();
    }
}