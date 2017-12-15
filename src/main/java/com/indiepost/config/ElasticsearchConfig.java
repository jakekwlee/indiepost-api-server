package com.indiepost.config;

import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@PropertySource("classpath:webapp-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "webapp.elasticsearch")
public class ElasticsearchConfig {

    private String clusterName;

    private String host;

    private int port;

    @Value("classpath:elasticsearch/settings.json")
    private Resource indexSettings;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @Bean
    public JestClientFactory jestClientFactory() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://" + host + ":" + port)
                .readTimeout(60000)
                .multiThreaded(true)
                .build());
        return factory;
    }

    @Bean
    public String indexSettings() {
        return getStringFromResource(indexSettings);
    }


    private String getStringFromResource(Resource resource) {
        try {
            InputStream inputStream = resource.getInputStream();
            return IOUtils.toString(inputStream)
                    .replaceAll("\\s", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
