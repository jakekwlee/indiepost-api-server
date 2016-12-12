package com.indiepost.model.response;

/**
 * Created by jake on 10/8/16.
 */
public class SimplifiedCategory {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String slug;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
