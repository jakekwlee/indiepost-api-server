package com.indiepost.model;


import javax.persistence.*;

@Entity
@Table(name = "Dictionary")
public class Word {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, unique = true)
    private String surface;
    private Integer cost;

    public Word() {
    }

    public Word(String surface) {
        this.surface = surface;
    }

    public Word(String surface, Integer cost) {
        this.surface = surface;
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
