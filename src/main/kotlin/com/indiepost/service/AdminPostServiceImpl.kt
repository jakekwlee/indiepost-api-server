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
import com.indiepost.repository.ContributorRepository
import com.indiepost.repository.ProfileRepository
import com.indiepost.repository.TagRepository
import com.indiepost.repository.elasticsearch.PostEsRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
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
            private val contributorRepository: ContributorRepository,
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
        addManyToManyAttributes(post, requestDto)
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

        addManyToManyAttributes(post, dto)
        return post.id
    }

    @Caching(evict = [
        CacheEvict(cacheNames = arrayOf("post::rendered"), key = "#requestDto.id"),
        CacheEvict(cacheNames = arrayOf("home::rendered"), allEntries = true),
        CacheEvict(cacheNames = arrayOf("category::rendered"), key = "#requestDto.categoryName.toLowerCase()")])
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

        addManyToManyAttributes(post, requestDto)

        val currentUser = userService.findCurrentUser()
        post.editor = currentUser
        post.modifiedAt = LocalDateTime.now()

        adminPostRepository.persist(post)
    }

    override fun updateAutosave(requestDto: AdminPostRequestDto) {
        val currentUser = userService.findCurrentUser()

        val post = adminPostRepository.findOne(requestDto.id)
                ?: throw EntityNotExistsException("No post with id with: " + requestDto.id)

        post.merge(requestDto)

        addManyToManyAttributes(post, requestDto)

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

    // using
    override fun find(status: PostStatus, pageable: Pageable): Page<AdminPostSummaryDto> {
        val currentUser = userService.findCurrentUser() ?: throw UnauthorizedException()
        val pageRequest = getPageable(pageable.pageNumber, pageable.pageSize, true)
        val result = adminPostRepository.find(currentUser, status, pageRequest)
        val count = adminPostRepository.count(status, currentUser)
        return if (result.isEmpty()) {
            PageImpl(emptyList(), pageRequest, 0)
        } else PageImpl(result, pageRequest, count)
    }

    override fun findIdsIn(ids: List<Long>, pageable: Pageable): Page<AdminPostSummaryDto> {
        val result = adminPostRepository.findByIdIn(ids)
        return if (result.isEmpty()) {
            PageImpl(emptyList(), PageRequest.of(0, pageable.pageSize), 0)
        } else PageImpl(result, PageRequest.of(0, pageable.pageSize), ids.size.toLong())
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
            postEs.categoryName?.let {
                highlight.categoryName = it
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

    private fun addManyToManyAttributes(post: Post, dto: AdminPostRequestDto) {
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
        if (dto.tags.isNotEmpty()) {
            val tagNames = dto.tags.stream()
                    .filter { (_, text) -> text != null }
                    .map<String> { (_, text) -> text!!.toLowerCase() }
                    .collect(Collectors.toList())

            val newTags = dto.tags.stream()
                    .filter { (id, text) -> id == null && text != null && text.isNotEmpty() }
                    .map<String> { (_, text) -> text!!.toLowerCase() }
                    .collect(Collectors.toList())
            for (text in newTags) {
                tagRepository.save(Tag(text))
            }

            val tags = tagRepository.findByNameIn(tagNames)
            post.addTags(tags)
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
