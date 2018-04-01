package com.indiepost.service;

import com.indiepost.dto.StaticPageDto;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Category;
import com.indiepost.repository.CategoryRepository;
import com.indiepost.repository.PostRepository;
import com.indiepost.repository.StaticPageRepository;
import cz.jiripinkas.jsitemapgenerator.WebPageBuilder;
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jake on 17. 3. 21.
 */
@Service
@Transactional(readOnly = true)
public class SitemapServiceImpl implements SitemapService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final StaticPageRepository staticPageRepository;

    @Autowired
    public SitemapServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository, StaticPageRepository staticPageRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.staticPageRepository = staticPageRepository;
    }

    @Override
    public String buildSitemap() {
        SitemapGenerator sitemapGenerator = new SitemapGenerator("http://www.indiepost.co.kr");

        List<PostSummaryDto> posts = postRepository.findByStatus(
                PostStatus.PUBLISH,
                PageRequest.of(0, 9999, Sort.Direction.DESC, "publishedAt")
        );

        for (PostSummaryDto postSummaryDto : posts) {
            sitemapGenerator.addPage(new WebPageBuilder()
                    .name("post/" + postSummaryDto.getId())
                    .changeFreqDaily()
                    .priorityMax()
                    .build()
            );
        }
        for (Category category : categoryRepository.findAll()) {
            sitemapGenerator.addPage(new WebPageBuilder()
                    .name("category/" + category.getSlug())
                    .changeFreqDaily()
                    .priorityMax()
                    .build()
            );
        }
        List<StaticPageDto> pageList = staticPageRepository
                .find(PageRequest.of(0, 100, Sort.Direction.ASC, "displayOrder"))
                .getContent();
        for (StaticPageDto page : pageList) {
            sitemapGenerator.addPage(new WebPageBuilder()
                    .name("page/" + page.getSlug())
                    .changeFreqDaily()
                    .priorityMax()
                    .build()
            );
        }

        return sitemapGenerator.constructSitemapString();
    }
}
