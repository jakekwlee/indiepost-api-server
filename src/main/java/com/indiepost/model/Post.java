package com.indiepost.model;

import com.indiepost.enums.Types;
import com.indiepost.model.analytics.Pageview;
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
    @JoinColumn(name = "originalId")
    private Post original;

    @Column(name = "originalId", nullable = false, insertable = false, updatable = false)
    private Long originalId;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("priority")
    private List<PostContributor> postContributors = new ArrayList<>();

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
    private String excerpt = "No Excerpt";

    @Column(nullable = false)
    @Size(max = 30)
    private String displayName = "Indiepost";

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
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

    @ManyToMany
    @OrderBy("id desc")
    @JoinTable(
            name = "Posts_Tags",
            joinColumns = {@JoinColumn(name = "postId")},
            inverseJoinColumns = {@JoinColumn(name = "tagId")}
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt")
    private List<Comment> comments;

    @Column(nullable = false)
    @Min(0)
    private int commentsCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("likedAt DESC")
    private List<Like> likes;

    @Column(nullable = false)
    @Min(0)
    private int likesCount = 0;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "post")
    private List<Pageview> pageviews;

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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
            tag.addPost(this);
        }
    }

    public Long removeTag(Tag tag) {
        Long tagId = tag.getId();
        if (this.tags.contains(tag)) {
            this.tags.remove(tag);
            tag.removePost(this);
        }
        return tagId;
    }

    public void clearTags() {
        this.tags.clear();
    }

    public void setPostContributors(List<PostContributor> postContributors) {
        this.postContributors = postContributors;
    }

    public void addContributor(Contributor contributor, int prioity) {
        PostContributor postContributor =
                new PostContributor(this, contributor, prioity);
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

    public List<Comment> getComments() {
        return comments;
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public Long removeComment(Comment comment) {
        Long commentId = comment.getId();
        this.comments.remove(comment);
        return commentId;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
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
}