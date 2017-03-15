package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 7/25/16.
 */

@Entity
@Table(name = "Tags")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 1, max = 50)
    @Column(nullable = false, unique = true)
    @Field
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @OrderBy(value = "publishedAt desc")
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPost(Post post) {
        if (!this.posts.contains(post)) {
            this.posts.add(post);
            post.addTag(this);
        }
    }

    public Long removePost(Post post) {
        Long postId = post.getId();
        if (this.posts.contains(post)) {
            this.posts.remove(post);
            post.removeTag(this);
        }
        return postId;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
