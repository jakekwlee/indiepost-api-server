package com.indiepost.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class ManagementToken {

    @Id
    private Integer id;

    @Size(max = 50)
    private String provider;

    @Size(max = 5000)
    private String accessToken;

    private LocalDateTime expireAt;

    public ManagementToken() {
    }

    public ManagementToken(String accessToken, LocalDateTime expireAt) {
        this.id = 1;
        this.accessToken = accessToken;
        this.expireAt = expireAt;
        this.provider = "AUTH0";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }
}
