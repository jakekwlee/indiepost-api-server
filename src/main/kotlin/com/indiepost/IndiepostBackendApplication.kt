package com.indiepost

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching


@SpringBootApplication
@EnableConfigurationProperties
@EnableCaching
class IndiepostBackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(IndiepostBackendApplication::class.java, *args)
}
