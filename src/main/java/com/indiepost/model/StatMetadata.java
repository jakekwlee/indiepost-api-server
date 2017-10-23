package com.indiepost.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by jake on 10/23/17.
 */
@Entity
@Table(name = "StatMetadata")
public class StatMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime postPageviewLastUpdatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getPostPageviewLastUpdatedAt() {
        return postPageviewLastUpdatedAt;
    }

    public void setPostPageviewLastUpdatedAt(LocalDateTime postPageviewLastUpdatedAt) {
        this.postPageviewLastUpdatedAt = postPageviewLastUpdatedAt;
    }
}
