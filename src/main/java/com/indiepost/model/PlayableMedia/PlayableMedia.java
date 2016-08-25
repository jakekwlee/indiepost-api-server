package com.indiepost.model.PlayableMedia;

import com.indiepost.model.Post;
import com.indiepost.model.converter.DurationToStringConverter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.util.Set;

/**
 * Created by jake on 7/24/16.
 */
abstract class PlayableMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(columnDefinition = "VARCHAR(100)")
    @Size(min = 6, max = 100)
    private String title;


    @NotNull
    @Column(columnDefinition = "VARCHAR(100)")
    @Size(min = 6, max = 100)
    private String location;

    @NotNull
    @Column(columnDefinition = "VARCHAR(100)")
    @Size(min = 6, max = 100)
    private String mimeType;

    @NotNull
    @Convert(converter = DurationToStringConverter.class)
    private Duration length;

    @NotNull
    private int Megabyte;

    @NotNull
    private Boolean isPaidContent = false;

    @NotNull
    @Min(0)
    private int price = 0;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "audios")
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Duration getLength() {
        return length;
    }

    public void setLength(Duration length) {
        this.length = length;
    }

    public int getMegabyte() {
        return Megabyte;
    }

    public void setMegabyte(int megabyte) {
        Megabyte = megabyte;
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