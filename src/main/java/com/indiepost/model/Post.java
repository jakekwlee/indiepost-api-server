package com.indiepost.model;

import com.indiepost.enums.Types;

import javax.persistence.*;
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

    private static final long serialVersionUID = -1121960490475976481L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originalId")
    private Post original;

    @Column(name = "originalId", nullable = false, insertable = false, updatable = false)
    private Long originalId;

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
    @Size(max = 400)
    private String excerpt = "";

    @Column(nullable = false)
    @Size(max = 30)
    private String displayName = "Indiepost";

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    private boolean showLastUpdated = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titleImageId")
    private ImageSet titleImage;

    @Column(name = "titleImageId", insertable = false, updatable = false)
    private Long titleImageId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Types.PostStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "authorId", nullable = false)
    private User author;

    @Column(name = "authorId", nullable = false, insertable = false, updatable = false)
    private Long authorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "editorId", nullable = false)
    private User editor;

    @Column(name = "editorId", nullable = false, insertable = false, updatable = false)
    private Long editorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @Column(name = "categoryId", nullable = false, insertable = false, updatable = false)
    private Long categoryId;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("priority")
    private List<PostTag> postTags = new ArrayList<>();

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
    private List<PostReading> postReadings = new ArrayList<>();

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Bookmark> postBookmarks = new ArrayList<>();

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("priority")
    private List<PostPost> postPosts = new ArrayList<>();

    public List<PostReading> getPostReadings() {
        return postReadings;
    }

    public void setPostReadings(List<PostReading> postReadings) {
        this.postReadings = postReadings;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public void addTag(Tag tag, int priority) {
        PostTag postTag = new PostTag(this, tag, priority);
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

    public void clearTags() {
        this.postTags.clear();
    }

    public void setPostContributors(List<PostContributor> postContributors) {
        this.postContributors = postContributors;
    }

    public void addContributor(Contributor contributor, int priority) {
        PostContributor postContributor =
                new PostContributor(this, contributor, priority);
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

    public void clearContributors() {
        this.postContributors.clear();
    }

    public void addRelatedPost(Post post, int priority) {
        PostPost postPost =
                new PostPost(this, post, priority);
        this.postPosts.add(postPost);
    }

    public void removeRelatedPost(Post post) {
        for (PostPost postPost : postPosts) {
            if (postPost.getPost().equals(this) &&
                    postPost.getRelatedPost().equals(post)) {
                postPosts.remove(postPost);
                postPost.getRelatedPost().getPostPosts().remove(this);
                postPost.setPost(null);
                postPost.setRelatedPost(null);
            }
        }
    }

    public List<Post> getPostPosts() {
        return postPosts.stream()
                .map(postPost -> postPost.getRelatedPost())
                .collect(Collectors.toList());
    }

    public void setPostPosts(List<PostPost> postPosts) {
        this.postPosts = postPosts;
    }

    public void clearRelatedPosts() {
        this.postPosts.clear();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public Post getOriginal() {
        return original;
    }

    public void setOriginal(Post original) {
        this.original = original;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
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

    public List<Bookmark> getPostBookmarks() {
        return postBookmarks;
    }

    public void setPostBookmarks(List<Bookmark> postBookmarks) {
        this.postBookmarks = postBookmarks;
    }

    public boolean isShowLastUpdated() {
        return showLastUpdated;
    }

    public void setShowLastUpdated(boolean showLastUpdated) {
        this.showLastUpdated = showLastUpdated;
    }
}