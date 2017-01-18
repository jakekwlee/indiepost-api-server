package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.indiepost.JsonView.Views;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.legacy.Contentlist;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Views.PublicList.class, Views.AdminList.class})
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originalId")
    private Post original;

    @Column(name = "originalId", nullable = false, insertable = false, updatable = false)
    private Long originalId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "legacyPostId")
    private Contentlist legacyPost;

    @Column(name = "legacyPostId", nullable = false, insertable = false, updatable = false)
    private Long legacyPostId;

    @Column(nullable = false)
    @JsonView({Views.PublicList.class, Views.AdminList.class})
    private boolean featured = false;

    @Column(nullable = false)
    @Size(max = 100)
    @JsonView({Views.PublicList.class, Views.AdminList.class})
    private String title = "No Title";

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @JsonView({Views.Public.class, Views.Admin.class})
    private String content = "";

    @Column(nullable = false)
    @Size(max = 300)
    @JsonView({Views.PublicList.class, Views.Admin.class})
    private String excerpt = "No Excerpt";

    @Column(nullable = false)
    @Size(max = 30)
    @JsonView({Views.PublicList.class, Views.AdminList.class})
    private String displayName = "Indiepost";

    @Column(nullable = false)
    @JsonView({Views.AdminList.class})
    private Date createdAt;

    @Column(nullable = false)
    @JsonView({Views.AdminList.class})
    private Date modifiedAt;

    @Column(nullable = false)
    @JsonView({Views.PublicList.class, Views.AdminList.class})
    private Date publishedAt;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "titleImageId")
    @JsonView({Views.PublicList.class, Views.Admin.class})
    private ImageSet titleImage;

    @Column(name = "titleImageId", insertable = false, updatable = false)
    private Long titleImageId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView({Views.PublicList.class, Views.AdminList.class})
    private PostEnum.Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView({Views.AdminList.class})
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
    @JsonView({Views.Public.class, Views.AdminList.class})
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt")
    @JsonView({Views.Public.class})
    private List<Comment> comments;

    @Column(nullable = false)
    @Min(0)
    @JsonView({Views.PublicList.class, Views.AdminList.class})
    private int commentsCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("likedAt DESC")
    @JsonView({Views.Public.class})
    private List<Like> likes;

    @Column(nullable = false)
    @Min(0)
    @JsonView({Views.PublicList.class, Views.AdminList.class})
    private int likesCount = 0;

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

    @JsonView({Views.Admin.class})
    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    @JsonView({Views.AdminList.class})
    public Long getLegacyPostId() {
        return legacyPostId;
    }

    public void setLegacyPostId(Long legacyPostId) {
        this.legacyPostId = legacyPostId;
    }

    @JsonView({Views.AdminList.class})
    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @JsonView({Views.AdminList.class})
    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    @JsonView({Views.PublicList.class, Views.AdminList.class})
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