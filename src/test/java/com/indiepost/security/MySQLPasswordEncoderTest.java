package com.indiepost.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by jake on 17. 1. 10.
 */
public class MySQLPasswordEncoderTest {

    @Test
    public void passwordEncoderShouldEncodeProperly() throws Exception {
        PasswordEncoder passwordEncoder = new MySQLPasswordEncoder();
        String rawPassword = "admin";
        String encodedPassword = "*4ACFE3202A5FF5CF467898FC58AAB1D615029441";
        Assert.assertEquals(encodedPassword, passwordEncoder.encode(rawPassword));
    }
}
