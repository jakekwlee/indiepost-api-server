package com.indiepost.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by jake on 7/22/17.
 */
@Configuration
@PropertySource("classpath:webapp-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "webapp.aws")
public class AwsConfig {
    ;
    private String s3BucketName;
    private boolean usingS3;

    public String getS3BucketName() {
        return s3BucketName;
    }

    public void setS3BucketName(String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }

    public boolean isUsingS3() {
        return usingS3;
    }

    public void setUsingS3(boolean usingS3) {
        this.usingS3 = usingS3;
    }
}
