package com.indiepost;

import com.indiepost.security.MySQLPasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class NewIndiepostApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewIndiepostApplication.class, args);
    }

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MySQLPasswordEncoder();
    }
}