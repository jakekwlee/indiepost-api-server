package com.indiepost.service;

import com.indiepost.dto.PageDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.repository.CategoryRepository;
import com.indiepost.repository.PageRepository;
import com.indiepost.repository.PostRepository;
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
    private final PageRepository pageRepository;

    @Autowired
    public SitemapServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository, PageRepository pageRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.pageRepository = pageRepository;
    }

    @Override
    public String buildSitemap() {
        SitemapGenerator sitemapGenerator = new SitemapGenerator("http://www.indiepost.co.kr");

        List<Post> posts = postRepository.findByStatus(
                PostStatus.PUBLISH,
                new PageRequest(0, 9999, Sort.Direction.DESC, "publishedAt")
        );

        for (Post post : posts) {
            sitemapGenerator.addPage(new WebPageBuilder()
                    .name("post/" + post.getId())
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
        List<PageDto> pageList = pageRepository.find(new PageRequest(0, 100, Sort.Direction.ASC, "displayOrder"));
        for (PageDto page : pageList) {
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
