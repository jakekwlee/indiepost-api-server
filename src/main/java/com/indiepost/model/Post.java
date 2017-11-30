package com.indiepost.model;

import com.indiepost.enums.Types;
import com.indiepost.model.analytics.Pageview;
import com.indiepost.model.legacy.LegacyPost;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "Posts")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originalId", insertable = false, updatable = false)
    private Post original;

    @Column(name = "originalId")
    private Long originalId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "legacyPostId")
    private LegacyPost legacyPost;

    @Column(name = "legacyPostId", insertable = false, updatable = false)
    private Long legacyPostId;

    @Column(nullable = false)
    private boolean featured = false;

    @Column(nullable = false)
    private boolean picked = false;

    @Column(nullable = false)
    private boolean splash = false;

    @Column(nullable = false)
    @Size(max = 100)
    private String title = "No Title";

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content = "";

    @Column(nullable = false)
    @Size(max = 300)
    private String excerpt = "";

    @Column(nullable = false)
    @Size(max = 30)
    private String bylineName = "Indiepost";

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "titleImageId", insertable = false, updatable = false)
    private ImageSet titleImage;

    @Column(name = "titleImageId")
    private Long titleImageId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Types.PostStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creatorId", nullable = false, insertable = false, updatable = false)
    private User creator;

    @Column(name = "creatorId", nullable = false)
    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modifiedUserId", nullable = false, insertable = false, updatable = false)
    private User modifiedUser;

    @Column(name = "modifiedUserId", nullable = false)
    private Long modifiedUserId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryId", nullable = false, insertable = false, updatable = false)
    private Category category;

    @Column(name = "categoryId", nullable = false)
    private Long categoryId = 2L;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("priority")
    private List<PostContributor> postContributors = new ArrayList<>();

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("priority asc, tag_id desc")
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<Bookmark> bookmarks;

    @Column(nullable = false)
    @Min(0)
    private int bookmarkCount = 0;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<Pageview> pageviews;

    public List<PostTag> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<PostTag> postTags) {
        this.postTags = postTags;
    }

    public List<Pageview> getPageviews() {
        return pageviews;
    }

    public void setPageviews(List<Pageview> pageviews) {
        this.pageviews = pageviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ImageSet getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(ImageSet titleImage) {
        this.titleImage = titleImage;
    }

    public Long getTitleImageId() {
        return titleImageId;
    }

    public void setTitleImageId(Long titleImageId) {
        this.titleImageId = titleImageId;
    }

    public String getBylineName() {
        return bylineName;
    }

    public void setBylineName(String bylineName) {
        this.bylineName = bylineName;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public Types.PostStatus getStatus() {
        return status;
    }

    public void setStatus(Types.PostStatus status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<PostContributor> getPostContributors() {
        return postContributors;
    }

    public void setPostContributors(List<PostContributor> postContributors) {
        this.postContributors = postContributors;
    }

    public void addContributor(Contributor contributor, int priority) {
        PostContributor postContributor =
                new PostContributor(this, contributor, LocalDateTime.now(), priority);
        this.postContributors.add(postContributor);
        contributor.getPostContributors().add(postContributor);
    }

    public void removeContributor(Contributor contributor) {
        for (Iterator<PostContributor> iterator = postContributors.iterator();
             iterator.hasNext(); ) {
            PostContributor postContributor = iterator.next();

            if (postContributor.getPost().equals(this) &&
                    postContributor.getContributor().equals(contributor)) {
                iterator.remove();
                postContributor.getContributor().getPostContributors().remove(postContributor);
                postContributor.setPost(null);
                postContributor.setContributor(null);
            }
        }
    }

    public List<Contributor> getContributors() {
        return postContributors.stream()
                .map(postContributor -> postContributor.getContributor())
                .collect(Collectors.toList());
    }

    public void addTag(Tag tag, int priority) {
        PostTag postTag = new PostTag(this, tag, LocalDateTime.now(), priority);
        this.postTags.add(postTag);
        tag.getPostTags().add(postTag);
    }

    public void removeTag(Tag tag) {
        for (Iterator<PostTag> iterator = postTags.iterator();
             iterator.hasNext(); ) {
            PostTag postTag = iterator.next();

            if (postTag.getPost().equals(this) &&
                    postTag.getTag().equals(tag)) {
                iterator.remove();
                postTag.getTag().getPostTags().remove(postTag);
                postTag.setPost(null);
                postTag.setTag(null);
            }
        }
    }

    public List<Tag> getTags() {
        return postTags.stream()
                .map(postTag -> postTag.getTag())
                .collect(Collectors.toList());
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(User modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Post getOriginal() {
        return original;
    }

    public void setOriginal(Post original) {
        this.original = original;
    }

    public LegacyPost getLegacyPost() {
        return legacyPost;
    }

    public void setLegacyPost(LegacyPost legacyPost) {
        this.legacyPost = legacyPost;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public Long getLegacyPostId() {
        return legacyPostId;
    }

    public void setLegacyPostId(Long legacyPostId) {
        this.legacyPostId = legacyPostId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifiedUserId() {
        return modifiedUserId;
    }

    public void setModifiedUserId(Long modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public boolean isSplash() {
        return splash;
    }

    public void setSplash(boolean splash) {
        this.splash = splash;
    }
}