package com.indiepost.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JacksonConfig {

    @Bean
    public RestTemplate restTemplate() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        int i = 0;

        // TODO 2018-4-22 fix this more elegantly
        // replace message converter
        RestTemplate restTemplate = new RestTemplate();
        for (HttpMessageConverter messageConverter : restTemplate.getMessageConverters()) {
            if (messageConverter.getClass() == MappingJackson2HttpMessageConverter.class) {
                break;
            }
            i++;
        }
        restTemplate.getMessageConverters().set(i, converter);
        return restTemplate;
    }
}
