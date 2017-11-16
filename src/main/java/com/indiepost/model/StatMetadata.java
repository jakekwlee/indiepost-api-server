package com.indiepost.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by jake on 10/29/17.
 */
@Entity
@Table(name = "StatMetadata")
public class StatMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime postStatsUpdatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPostStatsUpdatedAt() {
        return postStatsUpdatedAt;
    }

    public void setPostStatsUpdatedAt(LocalDateTime postStatsUpdatedAt) {
        this.postStatsUpdatedAt = postStatsUpdatedAt;
    }
}
