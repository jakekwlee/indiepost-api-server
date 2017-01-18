package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.indiepost.JsonView.Views;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jake on 7/25/16.
 */
@Entity
@Table(name = "Categories")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Views.AdminInit.class})
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private Category parent;

    @Column(name = "parentId", insertable = false, updatable = false)
    @JsonView({Views.AdminInit.class})
    private Long parentId;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Category> categories;

    @Column(nullable = false)
    @Size(min = 3, max = 20)
    @JsonView({Views.AdminInit.class})
    private String name;

    @Column(nullable = false)
    @Size(min = 3, max = 20)
    @JsonView({Views.AdminInit.class})
    private String slug;

    @Column(nullable = false)
    @JsonView({Views.AdminInit.class})
    private int displayOrder;

    @OneToMany(mappedBy = "category")
    private List<Post> posts;

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
