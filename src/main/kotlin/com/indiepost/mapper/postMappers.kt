package com.indiepost.mapper

import com.indiepost.dto.post.AdminPostRequestDto
import com.indiepost.dto.post.AdminPostResponseDto
import com.indiepost.dto.post.PostDto
import com.indiepost.dto.post.PostUserInteraction
import com.indiepost.enums.Types
import com.indiepost.model.Contributor
import com.indiepost.model.Post
import com.indiepost.model.PostReading
import com.indiepost.model.Tag
import com.indiepost.model.elasticsearch.PostEs
import com.indiepost.utils.DateUtil.instantToLocalDateTime
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import com.indiepost.utils.DomUtil.htmlToText
import org.springframework.beans.BeanUtils
import java.time.LocalDateTime
import java.util.stream.Collectors

fun Post.createDto(): PostDto {
    val postDto = PostDto()
    BeanUtils.copyProperties(this, postDto)
    this.publishedAt?.let {
        postDto.publishedAt = localDateTimeToInstant(it)
    }
    this.modifiedAt?.let {
        postDto.modifiedAt = localDateTimeToInstant(it)
    }
    this.category?.let {
        postDto.categoryName = it.name
    }
    return postDto
}

fun duplicate(srcPost: Post): Post {
    val destPost = Post()
    destPost.title = srcPost.title
    destPost.excerpt = srcPost.excerpt
    destPost.content = srcPost.content
    destPost.displayName = srcPost.displayName
    destPost.status = srcPost.status
    destPost.createdAt = srcPost.createdAt
    destPost.publishedAt = srcPost.publishedAt
    destPost.modifiedAt = srcPost.modifiedAt
    destPost.isShowLastUpdated = srcPost.isShowLastUpdated
    destPost.isSplash = srcPost.isSplash
    destPost.isFeatured = srcPost.isFeatured
    destPost.isPicked = srcPost.isPicked
    return destPost
}

fun Post.merge(requestDto: AdminPostRequestDto) {
    requestDto.title?.let {
        title = it
    }
    requestDto.content?.let {
        content = it
    }
    requestDto.excerpt?.let {
        excerpt = it
    }
    requestDto.publishedAt?.let {
        publishedAt = instantToLocalDateTime(it)
    }
    requestDto.displayName?.let {
        displayName = it
    }
    requestDto.status?.let {
        status = Types.PostStatus.valueOf(it.toUpperCase())
    }
    isSplash = requestDto.isSplash
    isFeatured = requestDto.isFeatured
    isPicked = requestDto.isPicked
    isShowLastUpdated = requestDto.isShowLastUpdated

    categoryId = requestDto.categoryId
    titleImageId = requestDto.titleImageId
    modifiedAt = LocalDateTime.now()
}

fun Post.addTags(tags: List<Tag>?) {
    if (tags == null) {
        return
    }
    clearTags()
    for ((index, tag) in tags.withIndex()) {
        addTag(tag, index)
    }
}

fun Post.addContributors(contributors: List<Contributor>?) {
    if (contributors == null) {
        return
    }
    clearContributors()
    for ((index, contributor) in contributors.withIndex()) {
        addContributor(contributor, index)
    }
}

fun AdminPostRequestDto.createPost(): Post {
    val post = Post()
    title?.let {
        post.title = it.trim()
    }
    content?.let {
        post.title = it.trim()
    }
    excerpt?.let {
        post.excerpt = it.trim()
    }
    displayName?.let {
        post.displayName = it.trim()
    }
    publishedAt?.let {
        post.publishedAt = instantToLocalDateTime(it)
    }

    post.isSplash = isSplash
    post.isFeatured = isFeatured
    post.isPicked = isPicked
    post.isShowLastUpdated = isShowLastUpdated

    post.titleImageId = titleImageId
    post.categoryId = categoryId
    post.createdAt = LocalDateTime.now()
    post.modifiedAt = LocalDateTime.now()
    return post
}

fun Post.createAdminPostResponseDto(): AdminPostResponseDto {
    val dto = AdminPostResponseDto()
    dto.id = id
    dto.title = title
    dto.content = content
    dto.excerpt = excerpt
    dto.displayName = displayName
    dto.titleImageId = titleImageId
    status?.let {
        dto.status = it.toString()
    }
    createdAt?.let {
        dto.createdAt = localDateTimeToInstant(it)
    }
    modifiedAt?.let {
        dto.modifiedAt = localDateTimeToInstant(it)
    }
    publishedAt?.let {
        dto.publishedAt = localDateTimeToInstant(it)
    }

    dto.categoryId = categoryId
    dto.authorId = authorId
    dto.editorId = editorId

    dto.isPicked = isPicked
    dto.isFeatured = isFeatured
    dto.isSplash = isSplash
    dto.isShowLastUpdated = isShowLastUpdated

    dto.authorName = author?.displayName
    dto.editorName = editor?.displayName
    dto.categoryName = category?.name
    dto.originalId = original?.id
    dto.originalStatus = original?.status.toString()
    titleImage?.let {
        dto.titleImage = it.createDto()
    }
    if (tags.isNotEmpty()) {
        dto.tags = tags.stream()
                .map<String> { (_, name) -> name }
                .collect(Collectors.toList())
    }
    if (contributors.isNotEmpty()) {
        dto.contributors = contributors.stream()
                .map<String> { (_, fullName) -> fullName }
                .collect(Collectors.toList())
    }
    if (getPostPosts().isNotEmpty()) {
        dto.relatedPostIds = getPostPosts().stream()
                .map<Long> { (id) -> id }
                .collect(Collectors.toList())
    }
    return dto
}

fun Post.createPostEs(): PostEs {
    val postEs = PostEs()
    postEs.id = id
    postEs.title = title
    postEs.creatorId = authorId
    postEs.modifiedUserId = editorId
    postEs.bylineName = displayName
    postEs.excerpt = excerpt
    status?.let {
        postEs.status = it.toString()
    }
    category?.let {
        postEs.categoryName = it.name
    }
    editor?.let {
        postEs.modifiedUserName = it.displayName
    }
    author?.let {
        postEs.creatorName = it.displayName
    }

    val contributors = contributors.stream()
            .map<String> { (_, fullName) -> fullName }
            .collect(Collectors.toList())
    postEs.setContributors(contributors)

    val tags = tags.stream()
            .map { (_, name) -> name!!.replace("_".toRegex(), " ") }
            .collect(Collectors.toList())
    postEs.setTags(tags)
    postEs.content = htmlToText(content)
    return postEs
}

fun PostReading.createPostUserInteraction(): PostUserInteraction {
    val dto = PostUserInteraction(this.postId)
    lastRead?.let {
        dto.lastRead = localDateTimeToInstant(it)
    }
    return dto
}

