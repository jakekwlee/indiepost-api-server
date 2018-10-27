package com.indiepost.service

import com.amazonaws.services.pinpoint.model.BadRequestException
import com.amazonaws.services.workdocs.model.EntityNotExistsException
import com.indiepost.dto.Highlight
import com.indiepost.dto.post.*
import com.indiepost.enums.Types.PostStatus
import com.indiepost.enums.Types.isPublicStatus
import com.indiepost.exceptions.UnauthorizedException
import com.indiepost.mapper.PostMapper.*
import com.indiepost.model.Post
import com.indiepost.model.Tag
import com.indiepost.repository.AdminPostRepository
import com.indiepost.repository.ContributorRepository
import com.indiepost.repository.TagRepository
import com.indiepost.repository.elasticsearch.PostEsRepository
import com.indiepost.utils.DateUtil.localDateTimeToInstant
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
            private val contributorRepository: ContributorRepository,
            private val postEsRepository: PostEsRepository,
            private val tagRepository: TagRepository) : AdminPostService {


    override fun createAutosave(requestDto: AdminPostRequestDto): Long? {
        val currentUser = userService.findCurrentUser()

        val post = Post()
        if (requestDto.id != null) {
            val originalPost = adminPostRepository.findOne(requestDto.id)
            post.original = originalPost
            post.originalId = originalPost!!.id
            post.author = post.author
        }

        copyDtoToPost(requestDto, post)

        if (requestDto.titleImageId != null) {
            post.titleImageId = requestDto.titleImageId
        }
        if (requestDto.categoryId != null) {
            post.categoryId = requestDto.categoryId
        }

        post.createdAt = LocalDateTime.now()
        post.modifiedAt = LocalDateTime.now()
        post.editor = currentUser
        if (post.author == null) {
            post.author = currentUser
        }
        post.status = PostStatus.AUTOSAVE

        val postId = adminPostRepository.persist(post)

        addContributors(post, requestDto.contributors)
        addTags(post, requestDto.tags)
        addRelatedPosts(post, requestDto.relatedPostIds)

        return postId
    }

    override fun findOne(id: Long): AdminPostResponseDto? {
        val post = adminPostRepository.findOne(id)
        return toAdminPostResponseDto(post)
    }

    override fun createDraft(dto: AdminPostRequestDto): Long? {
        val currentUser = userService.findCurrentUser()
        val post = copyDtoToPost(dto)

        val now = LocalDateTime.now()
        post.createdAt = now
        post.modifiedAt = now

        post.author = currentUser
        post.editor = currentUser

        if (dto.titleImageId != null) {
            post.titleImageId = dto.titleImageId
        }
        if (dto.categoryId != null) {
            post.categoryId = dto.categoryId
        }
        post.status = PostStatus.DRAFT
        adminPostRepository.persist(post)

        addContributors(post, dto.contributors)
        addTags(post, dto.tags)
        addRelatedPosts(post, dto.relatedPostIds)

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
        copyDtoToPost(requestDto, post)

        if (requestDto.titleImageId != null) {
            post.titleImageId = requestDto.titleImageId
        }
        if (requestDto.categoryId != null) {
            post.categoryId = requestDto.categoryId
        }
        addContributors(post, requestDto.contributors)
        addTags(post, requestDto.tags)
        addRelatedPosts(post, requestDto.relatedPostIds)

        val currentUser = userService.findCurrentUser()
        post.editor = currentUser
        post.modifiedAt = LocalDateTime.now()

        adminPostRepository.persist(post)
    }

    override fun updateAutosave(requestDto: AdminPostRequestDto) {
        val currentUser = userService.findCurrentUser()

        val post = adminPostRepository.findOne(requestDto.id)
                ?: throw EntityNotExistsException("No post with id with: " + requestDto.id)

        copyDtoToPost(requestDto, post)

        if (requestDto.titleImageId != null) {
            post.titleImageId = requestDto.titleImageId
        }
        if (requestDto.categoryId != null) {
            post.categoryId = requestDto.categoryId
        }
        addContributors(post, requestDto.contributors)
        addTags(post, requestDto.tags)
        addRelatedPosts(post, requestDto.relatedPostIds)

        post.modifiedAt = LocalDateTime.now()
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
            PageImpl(result, pageRequest, count)
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
            return PageImpl(ArrayList(), pageRequest, count.toLong())
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
        val (id1) = userService.findCurrentUser() ?: throw UnauthorizedException()
        if (changeTo == PostStatus.AUTOSAVE) {
            return
        }
        for (id in ids) {
            val post = adminPostRepository.findOne(id)
            if (post!!.titleImageId == null && isPublicStatus(changeTo)) {
                return
            }

            disableCurrentFeaturePostIfNeeded(changeTo, post.isSplash, post.isFeatured)
            // if original post is exists, unlink original
            if (post.originalId != null) {
                post.originalId = null
            }
            post.status = changeTo
            post.editorId = id1

            val now = LocalDateTime.now()
            post.modifiedAt = now
            adminPostRepository.persist(post)
        }
    }

    private fun getPageable(page: Int, maxResults: Int, isDesc: Boolean): Pageable {
        return if (isDesc)
            PageRequest.of(page, maxResults, Sort.Direction.DESC, "publishedAt")
        else
            PageRequest.of(page, maxResults, Sort.Direction.ASC, "publishedAt")
    }

    private fun addRelatedPosts(post: Post, relatedPostIds: List<Long>?) {
        if (relatedPostIds != null) {
            val relatedPosts = adminPostRepository.findByIds(relatedPostIds)
            post.clearRelatedPosts()
            for ((i, relatedPost) in relatedPosts.withIndex()) {
                post.addRelatedPost(relatedPost, i)
            }
        }
    }

    private fun addContributors(post: Post, contributorList: List<String>?) {
        if (contributorList != null) {
            val page = contributorRepository.findByFullNameIn(contributorList, PageRequest.of(0, 100))
            val contributors = page.content
            addContributorsToPost(post, contributors)
        }
    }

    private fun addTags(post: Post, tagList: List<String>?) {
        if (tagList != null && tagList.isNotEmpty()) {
            val tagListLowerCased = tagList.stream()
                    .map { t -> t.toLowerCase() }
                    .collect(Collectors.toList())
            var tags = tagRepository.findByNameIn(tagListLowerCased)
            val tagNames = tags.stream()
                    .map { (_, name) -> name!!.toLowerCase() }
                    .collect(Collectors.toList())
            val subList = tagListLowerCased.subtract(tagNames)
            if (!subList.isEmpty()) {
                for (name in subList) {
                    tagRepository.save(Tag(name.toLowerCase()))
                }
                tags = tagRepository.findByNameIn(tagListLowerCased)
            }
            addTagsToPost(post, tags)
        }
    }

    private fun toAdminPostResponseDto(post: Post?): AdminPostResponseDto? {
        if (post == null) {
            return null
        }
        val responseDto = AdminPostResponseDto()
        responseDto.id = post.id
        responseDto.title = post.title
        responseDto.content = post.content
        responseDto.excerpt = post.excerpt
        responseDto.displayName = post.displayName
        responseDto.titleImageId = post.titleImageId
        responseDto.status = post.status!!.toString()

        responseDto.createdAt = localDateTimeToInstant(post.createdAt!!)
        responseDto.modifiedAt = localDateTimeToInstant(post.modifiedAt!!)
        responseDto.publishedAt = localDateTimeToInstant(post.publishedAt!!)

        responseDto.categoryId = post.categoryId
        responseDto.authorId = post.authorId
        responseDto.editorId = post.editorId

        responseDto.isPicked = post.isPicked
        responseDto.isFeatured = post.isFeatured
        responseDto.isSplash = post.isSplash
        responseDto.isShowLastUpdated = post.isShowLastUpdated

        responseDto.authorName = post.author?.displayName
        responseDto.editorName = post.editor?.displayName
        responseDto.categoryName = post.category?.name
        responseDto.originalId = post.original?.id
        responseDto.originalStatus = post.original?.status.toString()
        if (post.titleImage != null) {
            val imageSetDto = imageSetToDto(post.titleImage!!)
            responseDto.titleImage = imageSetDto
        }
        if (!post.tags.isEmpty()) {
            val tags = post.tags.stream()
                    .map<String> { (_, name) -> name }
                    .collect(Collectors.toList())
            responseDto.tags = tags
        }
        if (!post.contributors.isEmpty()) {
            val contributors = post.contributors.stream()
                    .map<String> { (_, fullName) -> fullName }
                    .collect(Collectors.toList())
            responseDto.contributors = contributors
        }
        if (!post.getPostPosts().isEmpty()) {
            val ids = post.getPostPosts().stream()
                    .map<Long> { (id) -> id }
                    .collect(Collectors.toList())
            responseDto.relatedPostIds = ids
        }
        return responseDto
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
