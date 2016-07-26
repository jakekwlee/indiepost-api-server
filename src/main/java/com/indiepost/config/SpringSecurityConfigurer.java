package com.indiepost.config;

import com.indiepost.model.User.Roles;
import com.indiepost.service.IndiepostUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by jake on 7/26/16.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsServiceBean())
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new IndiepostUserDetailService();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/cms/**").hasAuthority(Roles.Editor.toString())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
        ;
    }
}