package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.net.MalformedURLException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by jake on 17. 3. 21.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class SitemapServiceTests {

    @Inject
    private SitemapService sitemapService;

    @Test
    public void sitemapShouldCreateCorrectly() throws MalformedURLException {
        String sitemap = sitemapService.buildSitemap();
        assertThat(sitemap).isNotEmpty();
        System.out.println(sitemap);
    }
}
