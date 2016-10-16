package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "menulist")
public class Menulist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private Long depth;

    private Long iorder;

    private Long parent;

    private String name;

    private String url;

    private Long content;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public Long getDepth() {
        return depth;
    }

    public void setDepth(Long depth) {
        this.depth = depth;
    }

    public Long getIorder() {
        return iorder;
    }

    public void setIorder(Long iorder) {
        this.iorder = iorder;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getContent() {
        return content;
    }

    public void setContent(Long content) {
        this.content = content;
    }
}
