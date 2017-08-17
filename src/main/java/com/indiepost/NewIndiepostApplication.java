package com.indiepost;

import com.indiepost.security.MySQLPasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class NewIndiepostApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewIndiepostApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MySQLPasswordEncoder();
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        return new HibernateAwareObjectMapper();
//    }
}