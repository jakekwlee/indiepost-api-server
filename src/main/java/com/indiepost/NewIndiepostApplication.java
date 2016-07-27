package com.indiepost;

import com.indiepost.security.MySQLPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class NewIndiepostApplication {

    private static final Logger log = LoggerFactory.getLogger(NewIndiepostApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NewIndiepostApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner demo(UserDAO userDAO) {
//        return (args) -> {
//            for (User user : userDAO.findAll()) {
//                log.info(user.toString());
//            }
//        };
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MySQLPasswordEncoder();
    }
}