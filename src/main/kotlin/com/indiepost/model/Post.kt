package com.indiepost.model

import com.indiepost.enums.Types
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import javax.persistence.*
import javax.validation.constraints.Size

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "Posts")
data class Post(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long? = null,

        @Column(nullable = false)
        var isFeatured: Boolean = false,

        @Column(nullable = false)
        var isPicked: Boolean = false,

        @Column(nullable = false)
        var isSplash: Boolean = false,

        @Column(nullable = false)
        @Size(max = 100)
        var title: String = "No Title",

        @Column(nullable = false, columnDefinition = "LONGTEXT")
        var content: String = "",

        @Column(nullable = false)
        @Size(max = 400)
        var excerpt: String = "",

        @Column(nullable = false)
        @Size(max = 30)
        var displayName: String = "INDIEPOST",

        @Column(nullable = false)
        var createdAt: LocalDateTime? = null,

        @Column(nullable = false)
        var modifiedAt: LocalDateTime? = null,

        @Column(nullable = false)
        var publishedAt: LocalDateTime? = null,

        @Column(nullable = false)
        var isShowLastUpdated: Boolean = false,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var status: Types.PostStatus? = null
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originalId")
    var original: Post? = null

    @Column(name = "originalId", nullable = false, insertable = false, updatable = false)
    var originalId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titleImageId")
    var titleImage: ImageSet? = null

    @Column(name = "titleImageId", insertable = false, updatable = false)
    var titleImageId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "authorId", nullable = false)
    var author: User? = null

    @Column(name = "authorId", nullable = false, insertable = false, updatable = false)
    var authorId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "editorId", nullable = false)
    var editor: User? = null

    @Column(name = "editorId", nullable = false, insertable = false, updatable = false)
    var editorId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryId", nullable = false)
    var category: Category? = null

    @Column(name = "categoryId", nullable = false, insertable = false, updatable = false)
    var categoryId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tagId", nullable = false)
    var primaryTag: Tag? = null

    @Column(name = "tagId", nullable = false, insertable = false, updatable = false)
    var primaryTagId: Long? = null

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("priority")
    private val postTags: MutableList<PostTag> = ArrayList()

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("priority")
    var postProfile: MutableList<PostProfile> = ArrayList()

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    var postReadings: MutableList<PostReading> = ArrayList()

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    var postBookmarks: MutableList<Bookmark> = ArrayList()

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("priority")
    private var postPosts: MutableList<PostPost> = ArrayList()

    val tags: MutableList<Tag>
        get() = postTags.stream()
                .map { postTag -> postTag.tag }
                .collect(Collectors.toList())

    val profiles: MutableList<Profile>
        get() = postProfile.stream()
                .map { postProfile -> postProfile.profile }
                .collect(Collectors.toList())

    fun addTag(tag: Tag, priority: Int) {
        val postTag = PostTag(this, tag, priority)
        this.postTags.add(postTag)
        tag.postTags.add(postTag)
    }

    fun removeTag(tag: Tag) {
        val iterator = postTags.iterator()
        while (iterator.hasNext()) {
            val postTag = iterator.next()

            if (postTag.post == this && postTag.tag == tag) {
                iterator.remove()
                postTag.tag?.let {
                    it.postTags.remove(postTag)
                }
                postTag.post = null
                postTag.tag = null
            }
        }
    }

    fun clearTags() {
        this.postTags.clear()
    }


    fun addProfile(profile: Profile, priority: Int) {
        val postProfile = PostProfile(this, profile, priority)
        this.postProfile.add(postProfile)
        profile.postProfile.add(postProfile)
    }

    fun removeProfile(profile: Profile) {
        val iterator = postProfile.iterator()
        while (iterator.hasNext()) {
            val postProfile = iterator.next()

            if (postProfile.post == this && postProfile.profile == profile) {
                iterator.remove()
                postProfile.profile?.let {
                    it.postProfile.remove(postProfile)
                }
                postProfile.post = null
                postProfile.profile = null
            }
        }
    }

    fun clearProfiles() {
        this.postProfile.clear()
    }

    fun addRelatedPost(post: Post, priority: Int) {
        val postPost = PostPost(this, post, priority)
        this.postPosts.add(postPost)
    }

    fun removeRelatedPost(post: Post) {
        for (postPost in postPosts) {
            if (postPost.post == this && postPost.relatedPost == post) {
                postPosts.remove(postPost)
                postPost.post = null
                postPost.relatedPost = null
            }
        }
    }

    fun getPostPosts(): MutableList<Post> {
        return postPosts.stream()
                .map { postPost -> postPost.relatedPost }
                .collect(Collectors.toList())
    }

    fun setPostPosts(postPosts: MutableList<PostPost>) {
        this.postPosts = postPosts
    }

    fun clearRelatedPosts() {
        this.postPosts.clear()
    }

    companion object {
        private const val serialVersionUID = -1121960490475976481L
    }
}