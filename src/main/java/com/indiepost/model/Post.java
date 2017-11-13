package com.indiepost.model;

import com.indiepost.enums.Types;
import com.indiepost.model.analytics.Pageview;
import com.indiepost.model.legacy.Contentlist;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.ko.*;
import org.apache.lucene.analysis.standard.ClassicFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "Posts")
@Indexed
@AnalyzerDef(name = "koreanHtmlTextAnalyzer",
        charFilters = {
                @CharFilterDef(factory = HTMLStripCharFilterFactory.class),
        },
        tokenizer = @TokenizerDef(factory = KoreanTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = ClassicFilterFactory.class),
                @TokenFilterDef(factory = KoreanFilterFactory.class, params = {
                        @Parameter(name = "hasOrigin", value = "true"),
                        @Parameter(name = "hasCNoun", value = "false"),
                        @Parameter(name = "exactMatch", value = "true"),
                        @Parameter(name = "bigrammable", value = "false")
                }),
                @TokenFilterDef(factory = HanjaMappingFilterFactory.class),
                @TokenFilterDef(factory = PunctuationDelimitFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class, params = {
                        @Parameter(name = "words", value = "org/apache/lucene/analysis/ko/stopwords.txt"),
                        @Parameter(name = "ignoreCase", value = "true")
                })
        }

)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Field(name = "id_sortable", analyze = Analyze.NO)
    @SortableField(forField = "id_sortable")
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private boolean featured = false;

    @Column(nullable = false)
    private boolean picked = false;

    @Column(nullable = false)
    private boolean splash = false;

    @Column(nullable = false)
    @Size(max = 100)
    @Field(boost = @Boost(value = 3f))
    @Analyzer(impl = KoreanAnalyzer.class)
    private String title = "No Title";

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    @Field
    @Analyzer(definition = "koreanHtmlTextAnalyzer")
    private String content = "";

    @Column(nullable = false)
    @Size(max = 300)
    @Field(boost = @Boost(value = 1.2f))
    @Analyzer(impl = KoreanAnalyzer.class)
    private String excerpt = "No Excerpt";

    @Column(nullable = false)
    @Size(max = 30)
    @Field(boost = @Boost(value = 2f))
    @Analyzer(impl = StandardAnalyzer.class)
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
    @JoinColumn(name = "creatorId", nullable = false)
    private User creator;

    @Column(name = "creatorId", nullable = false, insertable = false, updatable = false)
    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modifiedUserId", nullable = false)
    private User modifiedUser;

    @Column(name = "modifiedUserId", nullable = false, insertable = false, updatable = false)
    private Long modifiedUserId;

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
    @IndexedEmbedded
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt")
    private List<Comment> comments;

    @Column(nullable = false)
    @Min(0)
    private int commentsCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("likedAt DESC")
    private List<Bookmark> bookmarks;

    @Column(nullable = false)
    @Min(0)
    private int bookmarkCount = 0;

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

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
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