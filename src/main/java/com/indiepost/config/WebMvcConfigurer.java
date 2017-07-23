package com.indiepost.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by jake on 7/31/16.
 */
@Configuration
@EnableWebMvc
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder
                .createXmlMapper(false)
                .build();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        return objectMapper;
    }

//    @Bean
//    public ViewResolver viewResolver() {
////        ScriptTemplateViewResolver scriptTemplateViewResolver = new ScriptTemplateViewResolver("/public/", ".html");
////        scriptTemplateViewResolver.setOrder(1);
////        return scriptTemplateViewResolver;
//        V8ScriptTemplateViewResolver v8ScriptTemplateViewResolver = new V8ScriptTemplateViewResolver("/public/", ".html");
//        v8ScriptTemplateViewResolver.setOrder(1);
//        return v8ScriptTemplateViewResolver;
//    }
//
//    @Bean
//    public V8ScriptTemplateConfigurer v8ScriptTemplateConfigurer() {
//        return new V8ScriptTemplateConfigurer( "static/polyfill.js", "file:/data/static/resources/indiepost-react-webapp/dist/server.bundle.js");
//    }
//
////    public ScriptTemplateConfigurer scriptTemplateConfigurer() {
////        ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
////        configurer.setEngineName("nashorn");
////        configurer.setScripts(
////                "static/polyfill.js",
////                "file:/data/static/resources/indiepost-react-webapp/src/server.js"
////        );
////        configurer.setRenderFunction("render");
////        configurer.setSharedEngine(false);
////        return configurer;
////    }
}
