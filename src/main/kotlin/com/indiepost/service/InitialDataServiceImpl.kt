package com.indiepost.service

import com.indiepost.config.AppConfig
import com.indiepost.dto.InitialData
import com.indiepost.enums.Types
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Created by jake on 17. 1. 22.
 */
@Service
@Transactional(readOnly = true)
class InitialDataServiceImpl @Inject constructor(
        private val categoryService: CategoryService,
        private val userService: UserService,
        private val postService: PostService,
        private val staticPageService: StaticPageService,
        private val config: AppConfig,
        private val campaignService: CampaignService) : InitialDataService {

    override fun getInitialData(withLatestPosts: Boolean): InitialData {
        val initialData = InitialData()
        initialData.categories = categoryService.getDtoList()
        initialData.currentUser = userService.currentUserDto
        initialData.isWithLatestPosts = withLatestPosts
        val page = staticPageService.find(
                Types.PostStatus.PUBLISH, PageRequest.of(0, 100, Sort.Direction.DESC, "displayOrder"))
        initialData.staticPages = page.content

        initialData.splash = postService.findSplashPost()
        initialData.featured = postService.findFeaturePost()
        initialData.pickedPosts = postService.findPickedPosts()

        if (withLatestPosts) {
            initialData.posts = postService.find(PageRequest.of(0, config.fetchCount))
        }

        val banners = campaignService.findBannersOnCampaignPeriod()
        if (banners.isNotEmpty()) {
            initialData.banners = banners
        }

        val now = LocalDateTime.now()
        val topPosts = postService.findTopRatedPosts(now.minusDays(8L), now, 10)
        initialData.topPosts = topPosts
        return initialData
    }
}
