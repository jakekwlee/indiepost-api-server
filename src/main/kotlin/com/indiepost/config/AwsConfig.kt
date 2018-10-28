package com.indiepost.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

/**
 * Created by jake on 7/22/17.
 */
@Configuration
@PropertySource("classpath:webapp-\${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "webapp.aws")
data class AwsConfig(
        var s3BucketName: String? = null,

        var isUsingS3: Boolean? = false
)
