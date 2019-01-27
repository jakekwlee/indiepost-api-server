package com.indiepost.service

import com.amazonaws.services.pinpoint.model.BadRequestException
import com.amazonaws.services.workdocs.model.EntityNotExistsException
import com.indiepost.dto.Highlight
import com.indiepost.dto.post.*
import com.indiepost.enums.Types.Companion.isPublicStatus
import com.indiepost.enums.Types.PostStatus
import com.indiepost.exceptions.UnauthorizedException
import com.indiepost.mapper.*
import com.indiepost.model.Post
import com.indiepost.model.Tag
import com.indiepost.repository.AdminPostRepository
import com.indiepost.repository.ProfileRepository
import com.indiepost.repository.TagRepository
import com.indiepost.repository.elasticsearch.PostEsRepository
import com.indiepost.utils.DomUtil
import com.indiepost.utils.DomUtil.isBrokenYouTubeVideoIncluded
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

/**
 * Created by jake on 17. 1. 14.
 */
@Service
@Transactional
class AdminPostServiceImpl @Inject
constructor(private val userService: UserService,
            private val adminPostRepository: AdminPostRepository,
            private val profileRepository: ProfileRepository,
            private val postEsRepository: PostEsRepository,
            private val tagRepository: TagRepository) : AdminPostService {


    override fun createAutosave(requestDto: AdminPostRequestDto): Long? {
        val currentUser = userService.findCurrentUser()

        val post = Post()
        if (requestDto.id != null) {
            val originalPost = adminPostRepository.findOne(requestDto.id)
            if (originalPost != null) {
                post.original = originalPost
                post.originalId = originalPost.id
                post.author = post.author
            }
        }
        post.merge(requestDto)

        post.createdAt = LocalDateTime.now()
        post.modifiedAt = LocalDateTime.now()
        post.editor = currentUser
        if (post.author == null) {
            post.author = currentUser
        }
        post.status = PostStatus.AUTOSAVE

        val postId = adminPostRepository.persist(post)
        attachRelations(post, requestDto)
        return postId
    }

    override fun findOne(id: Long): AdminPostResponseDto? {
        val post = adminPostRepository.findOne(id)
        return post?.createAdminPostResponseDto()
    }

    override fun createDraft(dto: AdminPostRequestDto): Long? {
        val currentUser = userService.findCurrentUser()
        val post = dto.createPost()

        val now = LocalDateTime.now()
        post.createdAt = now
        post.modifiedAt = now

        post.author = currentUser
        post.editor = currentUser

        post.status = PostStatus.DRAFT
        adminPostRepository.persist(post)

        attachRelations(post, dto)
        return post.id
    }

    @Caching(evict = [
        CacheEvict(cacheNames = arrayOf("post::rendered"), key = "#requestDto.id"),
        CacheEvict(cacheNames = arrayOf("home::rendered"), allEntries = true),
        CacheEvict(cacheNames = arrayOf("latest::rendered"), allEntries = true)]
    )
    override fun update(requestDto: AdminPostRequestDto) {
        val status = PostStatus.valueOf(requestDto.status!!)
        disableCurrentFeaturePostIfNeeded(status, requestDto.isSplash, requestDto.isFeatured)

        val postId: Long?
        if (requestDto.originalId != null) {
            postId = requestDto.originalId
            // delete autosave
            adminPostRepository.deleteById(requestDto.id)
        } else {
            postId = requestDto.id
        }

        val post = adminPostRepository.findOne(postId)
                ?: throw EntityNotExistsException("No post with id with: " + requestDto.id)
        post.merge(requestDto)

        attachRelations(post, requestDto)

        val currentUser = userService.findCurrentUser()
        post.editor = currentUser
        post.modifiedAt = LocalDateTime.now()
        val content = post.content
        post.isBroken = isBrokenYouTubeVideoIncluded(content)
        adminPostRepository.persist(post)
    }

    override fun findIncludingBrokenLinks(pageable: Pageable): Page<AdminPostSummaryDto> {
        val count = adminPostRepository.countBrokenPosts()
        val result = adminPostRepository.findBrokenPosts(pageable)
        return if (result.isEmpty()) {
            PageImpl(emptyList(), pageable, count)
        } else PageImpl(result, pageable, count)
    }

    override fun findIdsIncludingBrokenVideo(): List<Long> {
        val posts = adminPostRepository.findContentListIncludingVideo()
        if (posts.isEmpty())
            return emptyList()
        val videoIdToPost = HashMap<String, Long>()
        for (post in posts) {
            val id = post.id ?: continue
            val content = post.content
            if (content.isNullOrBlank())
                continue
            val ids = DomUtil.extractYouTubeVideoIds(content)
            for (videoId in ids)
                videoIdToPost[videoId] = id
        }
        val partitions: List<List<String>> = videoIdToPost.keys.chunked(50)
        return partitions.parallelStream()
                .flatMap {
                    DomUtil.findBrokenYouTubeVideoByIds(it.toSet()).stream()
                }
                .map { videoIdToPost[it]!! }
                .distinct()
                .sorted(Comparator.reverseOrder<Long>())
                .collect(Collectors.toList())
    }

    override fun updateAutosave(requestDto: AdminPostRequestDto) {
        val currentUser = userService.findCurrentUser()

        val post = adminPostRepository.findOne(requestDto.id)
                ?: throw EntityNotExistsException("No post with id with: " + requestDto.id)

        post.merge(requestDto)
        attachRelations(post, requestDto)

        post.editor = currentUser
        post.status = PostStatus.AUTOSAVE

        adminPostRepository.persist(post)
    }

    override fun deleteById(id: Long): Long? {
        adminPostRepository.deleteById(id)
        return id
    }

    override fun delete(post: Post): Long? {
        adminPostRepository.delete(post)
        return post.id
    }

    override fun getPage(filter: PostFilter, pageable: Pageable): Page<AdminPostSummaryDto> {
        val query = filter.q
        val status = PostStatus.valueOf(filter.status.toUpperCase())
        val isBroken = filter.isBroken
        val tag = filter.tag
        val pageRequest = getPageable(pageable.pageNumber, pageable.pageSize, true)
        if (!query.isNullOrBlank()) {
            val id = query.toLongOrNull()
            return if (id != null)
                findIdsIn(Arrays.asList(id), pageRequest)
            else
                findText(query, status, pageRequest)
        }
        if (!isBroken.isNullOrBlank())
            return findIncludingBrokenLinks(pageRequest)
        val currentUser = userService.findCurrentUser() ?: throw UnauthorizedException()
        val result = adminPostRepository.find(
                currentUser = currentUser,
                status = status,
                tag = tag,
                pageable = pageRequest)
        val count = adminPostRepository.count(status = status, currentUser = currentUser, tag = tag)
        return if (result.isEmpty()) {
            PageImpl(emptyList(), pageRequest, 0)
        } else PageImpl(result, pageRequest, count)
    }

    override fun findIdsIn(ids: List<Long>, pageable: Pageable): Page<AdminPostSummaryDto> {
        val total = ids.size.toLong()
        val postIds = ids.stream()
                .skip(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .collect(Collectors.toList())
        if (postIds.isEmpty())
            return PageImpl(emptyList(), pageable, total)
        val result = adminPostRepository.findByIdIn(postIds)
        return if (result.isEmpty()) PageImpl(emptyList(), pageable, total)
        else PageImpl(result, pageable, ids.size.toLong())
    }

    override fun findText(text: String, status: PostStatus, pageable: Pageable): Page<AdminPostSummaryDto> {
        val currentUser = userService.findCurrentUser() ?: throw UnauthorizedException()
        return adminPostRepository.findText(text, currentUser, status, pageable)
    }

    override fun fullTextSearch(text: String, status: PostStatus,
                                pageable: Pageable): Page<AdminPostSummaryDto> {
        val currentUser = userService.findCurrentUser() ?: throw UnauthorizedException()
        val pageRequest = getPageable(pageable.pageNumber, pageable.pageSize, true)
        val postEsList = postEsRepository.search(text, status, currentUser, pageRequest)
        val count = postEsRepository.count(text, status, currentUser)

        if (postEsList.isEmpty()) {
            return PageImpl(emptyList(), pageRequest, count.toLong())
        }

        val ids = postEsList.stream()
                .map<Long> { (id) -> id }
                .collect(Collectors.toList())

        val dtoList = adminPostRepository.findByIdIn(ids)
        for ((index, dto) in dtoList.withIndex()) {
            val postEs = postEsList[index]
            val highlight = Highlight()
            var highlightExist = false
            postEs.title?.let {
                highlight.title = it
                highlightExist = true
            }
            postEs.bylineName?.let {
                highlight.bylineName = it
                highlightExist = true
            }
            postEs.creatorName?.let {
                highlight.creatorName = it
                highlightExist = true
            }
            postEs.modifiedUserName?.let {
                highlight.modifiedUserName = it
                highlightExist = true
            }
            if (highlightExist) {
                dto.highlight = highlight
            }
        }
        return PageImpl(dtoList, pageRequest, count.toLong())
    }

    override fun getAllTitles(): List<Title> {
        return adminPostRepository.getTitleList()
    }

    override fun count(): Long {
        return adminPostRepository.count()
    }

    override fun count(query: PostQuery): Long {
        return adminPostRepository.count(query)
    }

    override fun findAllBylineNames(): List<String> {
        return adminPostRepository.findAllDisplayNames()
    }

    override fun bulkDeleteByStatus(status: PostStatus) {
        val currentUser = userService.findCurrentUser() ?: throw UnauthorizedException()
        if (isPublicStatus(status)) {
            throw BadRequestException(
                    "It's prohibited directly bulk delete public status posts(PUBLIC|FUTURE|PENDING).")
        }
        val ids = adminPostRepository.findIds(currentUser, status)
        bulkDeleteByIds(ids)
    }

    override fun bulkDeleteByIds(ids: List<Long>) {
        for (id in ids) {
            deleteById(id)
        }
    }

    override fun bulkStatusUpdate(ids: List<Long>, changeTo: PostStatus) {
        val user = userService.findCurrentUser() ?: throw UnauthorizedException()
        val userId = user.id
        if (changeTo == PostStatus.AUTOSAVE) {
            return
        }
        for (id in ids) {
            val post = adminPostRepository.findOne(id) ?: continue
            if (post.titleImageId == null && isPublicStatus(changeTo)) {
                return
            }

            disableCurrentFeaturePostIfNeeded(changeTo, post.isSplash, post.isFeatured)
            // if original post is exists, unlink original
            post.originalId = null
            post.status = changeTo
            post.editorId = userId

            val now = LocalDateTime.now()
            post.modifiedAt = now
            adminPostRepository.persist(post)
        }
    }

    private fun getPageable(page: Int, maxResults: Int, isDesc: Boolean): Pageable {
        val direction = if (isDesc) Sort.Direction.DESC else Sort.Direction.ASC
        return PageRequest.of(page, maxResults, direction, "publishedAt")
    }

    private fun attachRelations(post: Post, dto: AdminPostRequestDto) {
        post.clearRelatedPosts()
        post.clearProfiles()
        post.clearTags()
        if (dto.relatedPostIds.isNotEmpty()) {
            val relatedPosts = adminPostRepository.findByIds(dto.relatedPostIds)
            post.clearRelatedPosts()
            for ((i, relatedPost) in relatedPosts.withIndex()) {
                post.addRelatedPost(relatedPost, i)
            }
        }
        if (dto.profiles.isNotEmpty()) {
            val profileIds = dto.profiles.stream().map { p -> p.id }.collect(Collectors.toList())
            if (profileIds.isNotEmpty()) {
                val profiles = profileRepository.findByIdIn(profileIds as List<Long>, PageRequest.of(0, 100))
                        .content
                post.addProfiles(profiles)
            }
        }
        if (dto.tags.isEmpty()) {
            post.primaryTag = null
            return
        }
        val tagListLowerCased = dto.tags.stream()
                .map { t -> t.toLowerCase() }
                .collect(Collectors.toList())
        var tags = tagRepository.findByNameIn(tagListLowerCased)
        val tagNames = tags.stream()
                .map { (_, name) -> name!!.toLowerCase() }
                .collect(Collectors.toList())
        val subList = tagListLowerCased.subtract(tagNames)
        if (!subList.isEmpty()) {
            for (name in subList) {
                tagRepository.save(Tag(name = name.toLowerCase()))
            }
            tags = tagRepository.findByNameIn(tagListLowerCased)
        }
        post.addTags(tags)
        val requestedPrimaryTagName = dto.primaryTag
        if (requestedPrimaryTagName.isNullOrEmpty()) {
            post.primaryTag = post.tags[0]
            return
        }
        // find primary tag from attached tag list
        val tag = post.tags.filter {
            it.name!!.toLowerCase() == requestedPrimaryTagName.toLowerCase()
        }
        if (tag.isNotEmpty()) {
            post.primaryTag = tag[0]
            return
        }
        val primaryTag = tagRepository.findByTagName(requestedPrimaryTagName)
        if (primaryTag != null) {
            post.addTag(primaryTag, 0)
            post.primaryTag = primaryTag
        }
    }

    private fun disableCurrentFeaturePostIfNeeded(status: PostStatus, isSplash: Boolean, isFeatured: Boolean) {
        if (status == PostStatus.PUBLISH) {
            if (isSplash) {
                adminPostRepository.disableSplashPosts()
            }
            if (isFeatured) {
                adminPostRepository.disableFeaturedPosts()
            }
        }
    }
}
