package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.legacy.Contentlist;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "Posts")
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "legacyPostId")
    @JsonIgnore
    private Contentlist legacyPost;

    @Column(name = "legacyPostId", nullable = false, insertable = false, updatable = false)
    private Long legacyPostId;

    @NotNull
    private boolean featured = false;

    @NotNull
    @Size(max = 100)
    private String title = "No Title";

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String content = "";

    @NotNull
    @Size(max = 300)
    private String excerpt = "No Excerpt";

    @NotNull
    @Size(max = 30)
    private String displayName = "Indiepost";

    @NotNull
    private Date createdAt;

    @NotNull
    private Date modifiedAt;

    @NotNull
    private Date publishedAt;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "titleImageId")
    private ImageSet titleImage;

    @Column(name = "titleImageId", insertable = false, updatable = false)
    private Long titleImageId;

    @NotNull
    @Min(0)
    private int likesCount = 0;

    @NotNull
    @Min(0)
    private int commentsCount = 0;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostEnum.Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostEnum.Type postType = PostEnum.Type.POST;

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

    @ManyToOne(optional = false)
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("likedAt DESC")
    private List<Like> likes;

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

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
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

    public PostEnum.Status getStatus() {
        return status;
    }

    public void setStatus(PostEnum.Status status) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
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

    public PostEnum.Type getPostType() {
        return postType;
    }

    public void setPostType(PostEnum.Type postType) {
        this.postType = postType;
    }

    public Post getOriginal() {
        return original;
    }

    public void setOriginal(Post original) {
        this.original = original;
    }

    public Contentlist getLegacyPost() {
        return legacyPost;
    }

    public void setLegacyPost(Contentlist legacyPost) {
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
}