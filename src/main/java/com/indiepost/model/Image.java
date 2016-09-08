package com.indiepost.model;

import com.indiepost.enums.IndiepostEnum.ImageSizeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jake on 8/15/16.
 */
@Entity
@Table(name = "Images")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Size(min = 2, max = 120)
    private String source;

    @NotNull
    private long fileSize;

    @NotNull
    private int width;

    @NotNull
    private int height;


    @Size(min = 9, max = 10)
    private String contentType;


    @NotNull
    private boolean isFeatured = false;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ImageSizeType sizeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageSetId")
    private ImageSet imageSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long size) {
        this.fileSize = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDirectory() {
        return source;
    }

    public void setDirectory(String source) {
        this.source = source;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ImageSet getImageSet() {
        return imageSet;
    }

    public void setImageSet(ImageSet imageSet) {
        this.imageSet = imageSet;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public ImageSizeType getSizeType() {
        return sizeType;
    }

    public void setSizeType(ImageSizeType sizeType) {
        this.sizeType = sizeType;
    }
}
