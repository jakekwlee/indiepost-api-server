package com.indiepost.model;

import com.indiepost.enums.ImageEnum.SizeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by jake on 8/15/16.
 */
@Entity
@Table(name = "Images")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 120)
    private String fileUrl;

    @NotNull
    @Size(min = 2, max = 120)
    private String fileName;

    @NotNull
    private long fileSize;

    @NotNull
    private int width;

    @NotNull
    private int height;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SizeType sizeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageSetId")
    private ImageSet imageSet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long size) {
        this.fileSize = size;
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public ImageSet getImageSet() {
        return imageSet;
    }

    public void setImageSet(ImageSet imageSet) {
        this.imageSet = imageSet;
    }

    public SizeType getSizeType() {
        return sizeType;
    }

    public void setSizeType(SizeType sizeType) {
        this.sizeType = sizeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
