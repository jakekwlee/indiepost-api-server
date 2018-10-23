package com.indiepost

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(NewIndiepostApplication::class))
@WebAppConfiguration
class NewIndiepostApplicationTests {

    @Test
    fun contextLoads() {
    }
}
