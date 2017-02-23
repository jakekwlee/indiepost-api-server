package com.indiepost.config;

import com.indiepost.view.V8ScriptTemplateConfigurer;
import com.indiepost.view.V8ScriptTemplateViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;
import org.springframework.web.servlet.view.script.ScriptTemplateView;
import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;

/**
 * Created by jake on 7/31/16.
 */
@Configuration
@EnableWebMvc
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    //    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
//            "classpath:/META-INF/resources/", "classpath:/resources/",
//            "classpath:/static/", "file:/data/indiepost-front-end/" };
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
    }

    @Bean
    public ViewResolver viewResolver() {
//        ScriptTemplateViewResolver scriptTemplateViewResolver = new ScriptTemplateViewResolver("/public/", ".html");
//        scriptTemplateViewResolver.setOrder(1);
//        return scriptTemplateViewResolver;
        V8ScriptTemplateViewResolver v8ScriptTemplateViewResolver = new V8ScriptTemplateViewResolver("/public/", ".html");
        v8ScriptTemplateViewResolver.setOrder(1);
        return v8ScriptTemplateViewResolver;
    }


    public ScriptTemplateConfigurer scriptTemplateConfigurer() {
        ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
        configurer.setEngineName("nashorn");
        configurer.setScripts(
                "static/polyfill.js",
                "file:/data/resources/indiepost-react-webapp/dist/server.bundle.js"
        );
        configurer.setRenderFunction("render");
        configurer.setSharedEngine(false);
        return configurer;
    }

    @Bean
    public V8ScriptTemplateConfigurer v8ScriptTemplateConfigurer() {
        return new V8ScriptTemplateConfigurer( "static/polyfill.js", "static/server.bundle.js");
    }
}
