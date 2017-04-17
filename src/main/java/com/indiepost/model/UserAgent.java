package com.indiepost.model;

import com.sangupta.murmur.Murmur2;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Created by jake on 17. 4. 9.
 */
@Entity
@Table(name = "UserAgents")
public class UserAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 2048)
    @Column(nullable = false, unique = true)
    private String uaString;

    @Column(nullable = false, unique = true)
    private Long uaHash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUaString() {
        return uaString;
    }

    public void setUaString(String uaString) {
        this.uaString = uaString;
    }

    public Long getUaHash() {
        return uaHash;
    }

    public void setUaHash(String uaString) {
        this.uaHash = Murmur2.hash64(uaString.getBytes(), uaString.length(), 1110L);
    }
}
