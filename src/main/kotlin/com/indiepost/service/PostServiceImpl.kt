package com.indiepost.service

import com.indiepost.dto.Highlight
import com.indiepost.dto.PageWithProfile
import com.indiepost.dto.Timeline
import com.indiepost.dto.TimelineRequest
import com.indiepost.dto.post.PostDto
import com.indiepost.dto.post.PostQuery
import com.indiepost.dto.post.PostSummaryDto
import com.indiepost.enums.Types.PostStatus
import com.indiepost.exceptions.ResourceNotFoundException
import com.indiepost.mapper.createDto
import com.indiepost.repository.*
import com.indiepost.repository.elasticsearch.PostEsRepository
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

/**
 * Created by jake on 7/30/16.
 */
@Service
@Transactional(readOnly = true)
class PostServiceImpl @Inject constructor(
        private val postRepository: PostRepository,
        private val profileRepository: ProfileRepository,
        private val statRepository: StatRepository,
        private val postEsRepository: PostEsRepository,
        private val postReadingRepository: PostReadingRepository,
        private val bookmarkRepository: BookmarkRepository,
        private val userRepository: UserRepository) : PostService {

    override fun findOne(postId: Long): PostDto {
        val post = postRepository.findById(postId) ?: throw ResourceNotFoundException()
        val dto = post.createDto()
        if (post.tags.isNotEmpty()) {
            val tags = post.tags.stream()
                    .map { (_, name) -> name!!.toLowerCase() }
                    .collect(Collectors.toList())
            dto.tags = tags
        }
        if (post.profiles.isNotEmpty()) {
            val profiles = post.profiles.stream()
                    .map { p -> p.createDto() }
                    .collect(Collectors.toList())
            dto.profiles = profiles
        }
        val user = userRepository.findCurrentUser()
        if (user != null) {
            val postReading = postReadingRepository.findOneByUserIdAndPostId(user.id!!, postId)
            val bookmark = bookmarkRepository.findOneByUserIdAndPostId(user.id!!, postId)
            if (postReading != null) {
                dto.lastRead = localDateTimeToInstant(postReading.lastRead!!)
            }
            if (bookmark != null) {
                dto.bookmarked = localDateTimeToInstant(bookmark.created!!)
            }
            dto.isInteractionFetched = true
        }
        return dto
    }

    override fun count(): Long {
        return postRepository.count()
    }

    override fun count(postQuery: PostQuery): Long {
        return postRepository.count(postQuery)
    }

    override fun findByIds(ids: List<Long>): List<PostSummaryDto> {
        return postRepository.findByIds(ids)
    }

    override fun findPublicPosts(pageable: Pageable): Page<PostSummaryDto> {
        return postRepository.findPublicPosts(getPageRequest(pageable), includeFeatured = false)
    }

    override fun findByTagName(tagName: String, pageable: Pageable, isPrimary: Boolean): Page<PostSummaryDto> {
        return if (isPrimary)
            postRepository.findByPrimaryTagName(tagName, pageable)
        else
            postRepository.findByTagName(tagName, getPageRequest(pageable))
    }

    override fun findReadingHistory(request: TimelineRequest): Timeline<PostSummaryDto> {
        val (userId) = userRepository.findCurrentUser() ?: return Timeline(emptyList(), request, 0)
        val result = postRepository.findReadingHistoryByUserId(userId!!, request)
        return addInteraction(result, request, userId, false)
    }

    override fun findBookmarks(request: TimelineRequest): Timeline<PostSummaryDto> {
        val (userId) = userRepository.findCurrentUser() ?: return Timeline(emptyList(), request, 0)
        val result = postRepository.findBookmarksByUserId(userId!!, request)
        return addInteraction(result, request, userId, true)
    }

    override fun findTopRatedPosts(since: LocalDateTime, until: LocalDateTime, limit: Int): List<PostSummaryDto> {
        val topStats = statRepository.getPostStatsOrderByPageviews(since, until, limit)
        if (topStats.isEmpty()) {
            return emptyList()
        }
        val topPostIds = topStats.stream()
                .map<Long> { (id) -> id }
                .collect(Collectors.toList())

        return postRepository.findByIds(topPostIds)
    }

    override fun findScheduledPosts(): List<PostSummaryDto> {
        return postRepository.findScheduledPosts()
    }

    override fun query(postQuery: PostQuery, pageable: Pageable): Page<PostSummaryDto> {
        return postRepository.query(postQuery, getPageRequest(pageable))
    }

    override fun fullTextSearch(text: String, pageable: Pageable): Page<PostSummaryDto> {
        var searchFor = text
        if (text.length > 30) {
            searchFor = searchFor.substring(0, 30)
        }
        searchFor = searchFor.toLowerCase()
        val pageRequest = getPageRequest(pageable)
        val postEsList = postEsRepository.search(searchFor, PostStatus.PUBLISH, pageRequest)
        if (postEsList.isEmpty()) {
            return postRepository.fallbackSearch(text, pageable)
        }
        val total = postEsRepository.count(searchFor, PostStatus.PUBLISH)
        val ids = postEsList.stream()
                .map<Long> { (id) -> id }
                .collect(Collectors.toList())
        val posts = postRepository.findByIds(ids)

        for ((index, dto) in posts.withIndex()) {
            val (_, title, excerpt) = postEsList[index]
            if (title == null && excerpt == null) {
                continue
            }
            val highlight = Highlight()
            highlight.title = title
            highlight.excerpt = excerpt
            dto.highlight = highlight
        }
        return PageImpl(posts, pageRequest, total.toLong())
    }

    override fun findRelatedPostsById(id: Long, pageable: Pageable): Page<PostSummaryDto> {
        val relatedPosts = postRepository.findRelatedPostsById(id).content
        val relatedPostIds = relatedPosts.stream()
                .map { it -> it.id }
                .collect(Collectors.toList())
        var moreLikeThis: List<PostSummaryDto>? = null
        if (pageable.pageSize > relatedPostIds.size) {
            val pageRequest = getPageRequest(pageable)
            val moreLikeThisIds = postEsRepository.moreLikeThis(listOf(id), PostStatus.PUBLISH, pageRequest)
            val subIds = moreLikeThisIds.subtract(relatedPostIds).toList()
            val ids = if (relatedPostIds.size + subIds.size > pageable.pageSize) {
                subIds.subList(0, pageable.pageSize - relatedPostIds.size)
            } else {
                subIds
            }
            moreLikeThis = postRepository.findByIds(ids as List<Long>)
        }
        val posts = ArrayList<PostSummaryDto>()
        if (relatedPosts.isNotEmpty()) {
            posts.addAll(relatedPosts)
        }
        if (moreLikeThis != null) {
            posts.addAll(moreLikeThis)
        }
        val pageRequest = getPageRequest(pageable)
        val total = posts.size.toLong()
        return PageImpl(posts, pageRequest, total)
    }

    override fun recommendations(pageable: Pageable): Page<PostSummaryDto> {
        val isEsAlive = postEsRepository.testConnection()
        if (!isEsAlive) {
            return defaultRecommendations(pageable)
        }
        val user = userRepository.findCurrentUser()
        val now = Instant.now().epochSecond

        val userId = user!!.id
        val watchedPosts = postRepository.findReadingHistoryByUserId(userId!!, TimelineRequest(now, 10))
        val bookmarkedPosts = postRepository.findBookmarksByUserId(userId, TimelineRequest(now, 10))

        if (watchedPosts.numberOfElements == 0 && bookmarkedPosts.numberOfElements == 0) {
            return defaultRecommendations(pageable)
        }

        val watchedPostIds = watchedPosts.content.stream()
                .map<Long> { post -> post.id }
                .collect(Collectors.toList())
        val bookmarkPostIds = bookmarkedPosts.content.stream()
                .map<Long> { p -> p.id }
                .collect(Collectors.toList())

        val resultIds = postEsRepository.recommendation(bookmarkPostIds, watchedPostIds, PostStatus.PUBLISH, pageable)

        // fallback
        if (resultIds.size < pageable.pageSize) {
            val lacks = pageable.pageSize - resultIds.size
            val result: MutableList<PostSummaryDto> = ArrayList()
            if (lacks > 0) {
                val posts = postRepository.findByIds(resultIds)
                if (posts.isNotEmpty()) {
                    result.addAll(posts)
                }
            }
            val topPosts = findTopRatedPosts(LocalDateTime.now().minusDays(60), LocalDateTime.now(), lacks)
            result.addAll(topPosts)
            return PageImpl(result, pageable, pageable.pageSize.toLong())
        }

        val posts = postRepository.findByIds(resultIds)
        val total = resultIds.size
        return PageImpl(posts, pageable, total.toLong())
    }

    override fun findByProfileSlug(slug: String, pageable: Pageable): PageWithProfile<PostSummaryDto> {
        val profile = profileRepository.findBySlug(slug)
        if (profile != null) {
            val page = postRepository.findByProfileSlug(slug, pageable)
            return PageWithProfile(profile.createDto(), page.content, page.pageable, page.totalElements)
        }
        return PageWithProfile()

    }

    override fun defaultRecommendations(pageable: Pageable): Page<PostSummaryDto> {
        val topPosts = findTopRatedPosts(LocalDateTime.now().minusDays(7), LocalDateTime.now(), pageable.pageSize)
        return PageImpl(topPosts, pageable, topPosts.size.toLong())
    }

    override fun findPickedPosts(): List<PostSummaryDto> {
        val postQuery = PostQuery.Builder(PostStatus.PUBLISH)
                .picked(true)
                .build()
        return query(postQuery, PageRequest.of(0, 4)).content
    }

    override fun findSplashPost(): PostSummaryDto? {
        val postQuery = PostQuery.Builder(PostStatus.PUBLISH)
                .splash(true)
                .build()
        val posts = query(postQuery, PageRequest.of(0, 1)).content
        return if (posts.isEmpty()) null else posts[0]
    }

    override fun findFeaturePost(): PostSummaryDto? {
        val postQuery = PostQuery.Builder(PostStatus.PUBLISH)
                .featured(true)
                .build()
        val posts = query(postQuery, PageRequest.of(0, 1)).content
        return if (posts.isEmpty()) null else posts[0]
    }

    private fun addInteraction(
            timeline: Timeline<PostSummaryDto>,
            request: TimelineRequest, userId: Long,
            bookmarked: Boolean): Timeline<PostSummaryDto> {
        if (timeline.content.isEmpty()) {
            return timeline
        }
        val posts = timeline.content
        val ids = posts.stream()
                .map<Long> { post -> post.id }
                .collect(Collectors.toList())

        val postReadings = postReadingRepository.findByUserIdAndPostIds(userId, ids)
        for ((_, _, postId, _, lastRead) in postReadings) {
            for (post in posts) {
                if (postId == post.id) {
                    post.isInteractionFetched = true
                    post.lastRead = localDateTimeToInstant(lastRead!!)
                    break
                }
            }
        }
        val oldest: Instant?
        val newest: Instant?

        if (bookmarked) {
            val bookmarks = bookmarkRepository.findByUserIdAndPostIds(userId, ids)
            for (bookmark in bookmarks) {
                for (post in posts) {
                    if (bookmark.postId == post.id) {
                        post.bookmarked = localDateTimeToInstant(bookmark.created!!)
                        break
                    }
                }
            }
            oldest = posts[posts.size - 1].bookmarked
            newest = posts[0].bookmarked
        } else {
            oldest = posts[posts.size - 1].lastRead
            newest = posts[0].lastRead
        }

        return Timeline(posts, request, newest!!, oldest!!, timeline.totalElements)
    }

    private fun getPageRequest(pageable: Pageable): Pageable {
        return PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                Sort.Direction.DESC,
                "publishedAt"
        )
    }
}
