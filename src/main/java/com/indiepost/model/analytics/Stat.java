package com.indiepost.model.analytics;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 4. 13.
 */
@Entity
@Table(name = "Stats", indexes = {
        @Index(columnList = "timestamp", name = "s_timestamp_idx")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "class", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Stat")
public class Stat implements Serializable {

    private static final long serialVersionUID = 7119668551684081952L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 200)
    private String path;

    @NotNull
    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitorId", updatable = false, insertable = false, nullable = false)
    private Visitor visitor;

    @NotNull
    @Column(name = "visitorId", nullable = false)
    private Long visitorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

}
