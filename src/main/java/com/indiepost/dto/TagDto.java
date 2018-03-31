package com.indiepost.dto;

/**
 * Created by jake on 10/8/16.
 */
public class TagDto {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    public TagDto() {
    }

    public TagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
