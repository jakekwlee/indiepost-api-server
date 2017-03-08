package com.indiepost.config;

import com.indiepost.filter.CORSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Created by jake on 7/31/16.
 */
@Configuration
@EnableWebMvc
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("file:/data/resources/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:/data/uploads/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        registry
                .addResourceHandler("/uploadData/**")
                .addResourceLocations("file:/data/uploadData/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CORSFilter());
        registration.addUrlPatterns("/api/**");
        registration.setOrder(1);
        return registration;
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
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
//        return new V8ScriptTemplateConfigurer( "static/polyfill.js", "file:/data/resources/indiepost-react-webapp/dist/server.bundle.js");
//    }
//
////    public ScriptTemplateConfigurer scriptTemplateConfigurer() {
////        ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
////        configurer.setEngineName("nashorn");
////        configurer.setScripts(
////                "static/polyfill.js",
////                "file:/data/resources/indiepost-react-webapp/src/server.js"
////        );
////        configurer.setRenderFunction("render");
////        configurer.setSharedEngine(false);
////        return configurer;
////    }
}
