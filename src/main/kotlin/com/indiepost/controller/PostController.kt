package com.indiepost.controller

import com.indiepost.dto.PostImageSetDto
import com.indiepost.dto.post.PostDto
import com.indiepost.dto.post.PostQuery
import com.indiepost.dto.post.PostSummaryDto
import com.indiepost.enums.Types
import com.indiepost.service.ImageService
import com.indiepost.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

/**
 * Created by jake on 7/31/16.
 */
@RestController
@RequestMapping("/posts")
class PostController @Inject
constructor(private val postService: PostService, private val imageService: ImageService) {

    @GetMapping("/future")
    fun getScheduledPosts(): List<PostSummaryDto> = postService.findScheduledPosts()

    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): PostDto {
        return postService.findOne(id)
    }

    @GetMapping
    fun getPosts(@RequestParam splash: Boolean = false,
                 @RequestParam featured: Boolean = false,
                 @RequestParam picked: Boolean = false,
                 pageable: Pageable): Page<PostSummaryDto> {
        val postQuery = PostQuery.Builder(Types.PostStatus.PUBLISH)
                .splash(splash)
                .featured(featured)
                .picked(picked)
                .build()
        return postService.query(postQuery, pageable)
    }

    @GetMapping("/category/{slug}")
    fun getPostsByCategoryName(@PathVariable slug: String, pageable: Pageable): Page<PostSummaryDto> {
        return postService.findByCategorySlug(slug, pageable)
    }

    @GetMapping("/tag/{tagName}")
    fun getPostsByTagName(@PathVariable tagName: String, pageable: Pageable): Page<PostSummaryDto> {
        return postService.findByTagName(tagName, pageable)
    }

    @GetMapping("/contributor/{fullName}")
    fun getPostsByContributorName(@PathVariable fullName: String, pageable: Pageable): Page<PostSummaryDto> {
        return postService.findByContributorFullName(fullName, pageable)
    }

    @GetMapping("/search/{searchText}")
    fun fullTextSearch(@PathVariable searchText: String, pageable: Pageable): Page<PostSummaryDto> {
        return postService.fullTextSearch(searchText, pageable)
    }

    @GetMapping("/related/{id}")
    fun getRelatedPosts(@PathVariable id: Long, pageable: Pageable): Page<PostSummaryDto> {
        return postService.findRelatedPostsById(id, pageable)
    }

    @GetMapping("/images/{id}")
    fun getImagesOnPost(@PathVariable id: Long?): PostImageSetDto {
        return imageService.findImagesOnPost(id!!)
    }
}
