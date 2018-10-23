package com.indiepost.service

import com.indiepost.NewIndiepostApplication
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject

/**
 * Created by jake on 17. 3. 21.
 */

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(NewIndiepostApplication::class))
@WebAppConfiguration
class SitemapServiceTests {

    @Inject
    private lateinit var sitemapService: SitemapService

    @Test
    fun sitemapShouldCreateCorrectly() {
        val sitemap = sitemapService.buildSitemap()
        assertThat(sitemap).isNotEmpty()
        println(sitemap)
    }
}
