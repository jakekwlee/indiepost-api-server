package com.indiepost.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

/**
 * Created by jake on 17. 2. 26.
 */
@Configuration
@PropertySource("classpath:webapp-\${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "webapp")
data class AppConfig(
        var baseUrl: String? = null,

        var cdnUrl: String? = null,

        var staticRootPath: String? = null,

        var imageUploadPath: String? = null,

        var mediaUploadPath: String? = null,

        var imageFilenameLength: Int = 0,

        var imageFilenameFormat: String? = null,

        var acceptedImageTypes: List<String>? = null,

        var fetchCount: Int = 0
)
