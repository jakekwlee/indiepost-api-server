package com.indiepost;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class NewIndiepostApplicationTests {

    @Test
    public void contextLoads() {
    }
}
