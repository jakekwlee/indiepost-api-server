package com.indiepost.mapper

import com.indiepost.dto.ProfileSummaryDto
import com.indiepost.dto.post.AdminPostRequestDto
import com.indiepost.dto.post.AdminPostResponseDto
import com.indiepost.dto.post.PostDto
import com.indiepost.dto.post.PostUserInteraction
import com.indiepost.enums.Types
import com.indiepost.model.Post
import com.indiepost.model.PostReading
import com.indiepost.model.Profile
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
    this.titleImage?.let {
        postDto.titleImage = it.createDto()
    }
    return postDto
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

fun Post.addProfiles(profiles: List<Profile>?) {
    if (profiles == null) {
        return
    }
    clearProfiles()
    for ((index, profile) in profiles.withIndex()) {
        addProfile(profile, index)
    }
}

fun AdminPostRequestDto.createPost(): Post {
    val post = Post()
    title?.let {
        post.title = it.trim()
    }
    content?.let {
        post.content = it.trim()
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
    original?.let {
        dto.originalStatus = it.status.toString()
    }
    titleImage?.let {
        dto.titleImage = it.createDto()
    }
    if (tags.isNotEmpty()) {
        dto.tags = tags.stream()
                .map<String> { (_, name) -> name }
                .collect(Collectors.toList())
    }
    if (profiles.isNotEmpty()) {
        dto.profiles = profiles.stream()
                .map { p -> ProfileSummaryDto(p.id, p.fullName, p.profileType.toString()) }
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

    val profiles = profiles.stream()
            .map<String> { (_, displayName) -> displayName }
            .collect(Collectors.toList())
    postEs.setProfiles(profiles)

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

