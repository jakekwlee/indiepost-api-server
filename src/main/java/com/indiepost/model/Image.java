package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.indiepost.JsonView.Views;
import com.indiepost.enums.ImageEnum.SizeType;

import javax.persistence.*;
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
    @JsonView({Views.PublicList.class, Views.Admin.class})
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 120)
    @JsonView({Views.PublicList.class, Views.Admin.class})
    private String fileUrl;

    @Column(nullable = false)
    @Size(min = 2, max = 120)
    @JsonView({Views.Admin.class})
    private String fileName;

    @Column(nullable = false)
    @JsonView({Views.Admin.class})
    private long fileSize;

    @Column(nullable = false)
    @JsonView({Views.PublicList.class, Views.Admin.class})
    private int width;

    @Column(nullable = false)
    @JsonView({Views.PublicList.class, Views.Admin.class})
    private int height;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView({Views.Admin.class})
    private SizeType sizeType;

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
