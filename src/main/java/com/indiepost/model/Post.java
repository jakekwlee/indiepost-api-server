package com.indiepost.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "Posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Size(min = 2, max = 100)
    private String title;

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    @Size(min = 2)
    private String content;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservedAt;

    @NotNull
    @Size(min = 5, max = 100)
    private String titleImage;

    @NotNull
    @Size(min = 2, max = 300)
    private String excerpt;

    @NotNull
    @Min(0)
    private int likesCount = 0;

    @NotNull
    @Min(0)
    private int bookmarkedCount = 0;

    @NotNull
    @Min(0)
    private int commentsCount = 0;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "editorId", nullable = false)
    private User editor;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId", nullable = false)
    private User author;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Posts_MediaContents",
            joinColumns = {@JoinColumn(name = "postId")},
            inverseJoinColumns = {@JoinColumn(name = "mediaContentId")}
    )
    private Set<MediaContent> mediaContents;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Posts_Tags",
            joinColumns = {@JoinColumn(name = "postId")},
            inverseJoinColumns = {@JoinColumn(name = "tagId")}
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OrderBy("createdAt")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("likedAt DESC")
    private Set<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy(value = "bookmarkedAt DESC")
    private Set<Bookmark> bookmarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Date getCreateAt() {
        return createdAt;
    }

    public void setCreateAt(Date createAt) {
        this.createdAt = createAt;
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

    public Date getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Date reservedAt) {
        this.reservedAt = reservedAt;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
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

    public int getBookmarkedCount() {
        return bookmarkedCount;
    }

    public void setBookmarkedCount(int bookmarkedCount) {
        this.bookmarkedCount = bookmarkedCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<MediaContent> getMediaContents() {
        return mediaContents;
    }

    public void setMediaContents(Set<MediaContent> mediaContents) {
        this.mediaContents = mediaContents;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Set<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public enum Status {
        DELETED, DRAFT, RESERVED, PUBLISHED
    }
}
