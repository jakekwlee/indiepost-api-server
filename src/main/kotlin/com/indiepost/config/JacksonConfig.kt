package com.indiepost.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class JacksonConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        val converter = MappingJackson2HttpMessageConverter()
        converter.objectMapper = mapper
        var i = 0

        // TODO 2018-4-22 fix this more elegantly
        // replace message converter
        val restTemplate = RestTemplate()
        for (messageConverter in restTemplate.messageConverters) {
            if (messageConverter.javaClass == MappingJackson2HttpMessageConverter::class.java) {
                break
            }
            i++
        }
        restTemplate.messageConverters[i] = converter
        return restTemplate
    }
}
