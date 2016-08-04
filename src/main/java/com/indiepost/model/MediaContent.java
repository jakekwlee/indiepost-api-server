package com.indiepost.model;

import com.sun.org.apache.xpath.internal.operations.String;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by jake on 7/24/16.
 */
@Entity
@Table(name = "MediaContents")
public class MediaContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(columnDefinition = "VARCHAR(100)")
    @Size(min = 6, max = 100)
    private String location;

    @NotNull
    @Column(columnDefinition = "VARCHAR(100)")
    @Size(min = 6, max = 100)
    private String mimeType;
//
//    private int width;
//
//    private int height;
//
//    @Size(min = 6, max = 100)
//    private String thumbnail;
//
//    private int thumbnailWidth;
//
//    private int thumbnailHeight;
//
//    @Size(min = 6, max = 6)
//    private String color;

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
//
//    public int getWidth() {
//        return width;
//    }
//
//    public void setWidth(int width) {
//        this.width = width;
//    }
//
//    public int getHeight() {
//        return height;
//    }
//
//    public void setHeight(int height) {
//        this.height = height;
//    }
//
//    public String getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }
//
//    public int getThumbnailWidth() {
//        return thumbnailWidth;
//    }
//
//    public void setThumbnailWidth(int thumbnailWidth) {
//        this.thumbnailWidth = thumbnailWidth;
//    }
//
//    public int getThumbnailHeight() {
//        return thumbnailHeight;
//    }
//
//    public void setThumbnailHeight(int thumbnailHeight) {
//        this.thumbnailHeight = thumbnailHeight;
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }

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