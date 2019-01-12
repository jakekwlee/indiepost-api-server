package com.indiepost.service

import com.indiepost.repository.PostRepository
import com.indiepost.repository.StaticPageRepository
import com.indiepost.repository.TagRepository
import cz.jiripinkas.jsitemapgenerator.WebPageBuilder
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.inject.Inject

/**
 * Created by jake on 17. 3. 21.
 */
@Service
@Transactional(readOnly = true)
class SitemapServiceImpl @Inject constructor(
        private val postRepository: PostRepository,
        private val tagRepository: TagRepository,
        private val staticPageRepository: StaticPageRepository) : SitemapService {

    override fun buildSitemap(): String {
        val sitemapGenerator = SitemapGenerator("http://www.indiepost.co.kr")

        val posts = postRepository.findPublicPosts(
                PageRequest.of(0, 9999, Sort.Direction.DESC, "publishedAt")
        ).content

        for (postSummaryDto in posts) {
            sitemapGenerator.addPage(WebPageBuilder()
                    .name("post/" + postSummaryDto.id!!)
                    .changeFreqDaily()
                    .priorityMax()
                    .build()
            )
        }
        for (tag in tagRepository.findSelected()) {
            sitemapGenerator.addPage(WebPageBuilder()
                    .name("tag/$tag?primary=true")
                    .changeFreqDaily()
                    .priorityMax()
                    .build()
            )
        }
        val pageList = staticPageRepository
                .find(PageRequest.of(0, 100, Sort.Direction.ASC, "displayOrder"))
                .content
        for (page in pageList) {
            if (page.slug == null) {
                continue
            }
            sitemapGenerator.addPage(WebPageBuilder()
                    .name("page/${page.slug}")
                    .changeFreqDaily()
                    .priorityMax()
                    .build()
            )
        }

        return sitemapGenerator.constructSitemapString()
    }
}
