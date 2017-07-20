package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.MalformedURLException;

/**
 * Created by jake on 17. 3. 21.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class SitemapServiceTest {
    @Autowired
    private SitemapService sitemapService;

    @Test
    public void sitemapShouldCreateCorrectly() throws MalformedURLException {
        sitemapService.buildSitemap();
    }
}
