package com.indiepost;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class NewIndiepostApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void contextLoads() {
    }

    @Test
    public void passwordEncoderShouldEncodeProperly() throws Exception {
        String rawPassword = "admin";
        String encodedPassword = "*4ACFE3202A5FF5CF467898FC58AAB1D615029441";
        Assert.assertEquals(encodedPassword, this.passwordEncoder.encode(rawPassword));
    }
}
