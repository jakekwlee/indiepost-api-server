package com.indiepost.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jake on 8/31/17.
 */
public class SuggestionDto {
    private static final long serialVersionUID = 1L;

    private Long userId;

    @NotNull
    @Size(min = 1, max = 200)
    private String subject;

    @NotNull
    @Size(min = 10, max = 10000)
    private String content;

    @NotNull
    @Size(min = 1, max = 100)
    private String proposer;

    @NotNull
    @Email
    @Size(min = 1, max = 100)
    private String email;

    private String contact;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
