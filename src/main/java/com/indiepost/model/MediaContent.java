package com.indiepost.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "MediaContents")
public class MediaContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Size(min = 6, max = 100)
    private String location;

    @NotNull
    @Size(min = 6, max = 100)
    private String mimeType;


    @NotNull
    private Boolean isPaidContent = false;

    @NotNull
    @Min(0)
    private int price = 0;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "mediaContents")
    private Set<Post> posts;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Boolean getPaidContent() {
        return isPaidContent;
    }

    public void setPaidContent(Boolean paidContent) {
        isPaidContent = paidContent;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
