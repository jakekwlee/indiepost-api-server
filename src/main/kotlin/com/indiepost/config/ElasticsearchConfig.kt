package com.indiepost.config

import io.searchbox.client.JestClientFactory
import io.searchbox.client.config.HttpClientConfig
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.Resource
import java.io.IOException

@Configuration
@PropertySource("classpath:webapp-\${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "webapp.elasticsearch")
class ElasticsearchConfig {

    var clusterName: String? = null

    var host: String? = null

    var port: Int = 0

    @Value("classpath:elasticsearch/settings.json")
    private lateinit var indexSettings: Resource

    @Bean
    fun jestClientFactory(): JestClientFactory {
        val factory = JestClientFactory()
        factory.setHttpClientConfig(HttpClientConfig.Builder("http://$host:$port")
                .readTimeout(30000)
                .multiThreaded(true)
                .build())
        return factory
    }

    @Bean
    fun indexSettings(): String? {
        return getStringFromResource(indexSettings)
    }

    private fun getStringFromResource(resource: Resource): String? {
        try {
            val inputStream = resource.inputStream
            return IOUtils.toString(inputStream, Charsets.UTF_8)
                    .replace("\\s".toRegex(), "")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

}
