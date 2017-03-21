package com.indiepost.service;

import com.indiepost.config.HomeConfig;
import com.indiepost.dto.PageDto;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.repository.CategoryRepository;
import com.indiepost.repository.PageRepository;
import com.indiepost.repository.PostRepository;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by jake on 17. 3. 21.
 */
@Service
@Transactional(readOnly = true)
public class SitemapServiceImpl implements SitemapService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PageRepository pageRepository;
    private final HomeConfig homeConfig;
    private String BASE_URL;
    private String SITEMAP_DIR_PATH;

    @Autowired
    public SitemapServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository, PageRepository pageRepository, HomeConfig homeConfig) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.pageRepository = pageRepository;
        this.homeConfig = homeConfig;
        this.BASE_URL = this.homeConfig.getBaseUrl();
        this.SITEMAP_DIR_PATH = this.homeConfig.getResourcesPath();
    }

    @Override
    public void createSitemap() throws MalformedURLException {
        File targetDirectory = new File(SITEMAP_DIR_PATH);
        WebSitemapGenerator webSitemapGenerator = WebSitemapGenerator.builder(BASE_URL, targetDirectory)
                .allowMultipleSitemaps(false)
                .gzip(true)
                .build();

        List<PostSummaryDto> posts = postRepository.findByStatus(
                PostEnum.Status.PUBLISH,
                new PageRequest(0, 50000, Sort.Direction.DESC, "publishedAt")
        );
        for (PostSummaryDto postSummaryDto : posts) {
            WebSitemapUrl webSitemapUrl = new WebSitemapUrl.Options(BASE_URL + "/post/" + postSummaryDto.getId())
                    .lastMod(postSummaryDto.getPublishedAt())
                    .priority(0.9)
                    .changeFreq(ChangeFreq.WEEKLY)
                    .build();
            webSitemapGenerator.addUrl(webSitemapUrl);
        }
        for (Category category : categoryRepository.findAll()) {
            WebSitemapUrl webSitemapUrl = new WebSitemapUrl.Options(BASE_URL + "/category/" + category.getSlug())
                    .lastMod(posts.get(0).getPublishedAt())
                    .priority(0.9)
                    .changeFreq(ChangeFreq.DAILY)
                    .build();
            webSitemapGenerator.addUrl(webSitemapUrl);
        }
        List<PageDto> pageDtoList = pageRepository.find(new PageRequest(0, 100, Sort.Direction.ASC, "displayOrder"));
        for (PageDto page : pageDtoList) {
            WebSitemapUrl webSitemapUrl = new WebSitemapUrl.Options(BASE_URL + "/page/" + page.getSlug())
                    .lastMod(page.getModifiedAt())
                    .priority(0.9)
                    .changeFreq(ChangeFreq.MONTHLY)
                    .build();
            webSitemapGenerator.addUrl(webSitemapUrl);
        }

        webSitemapGenerator.write();
    }
}
