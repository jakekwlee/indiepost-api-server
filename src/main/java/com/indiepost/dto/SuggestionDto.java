package com.indiepost.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

/**
 * Created by jake on 8/31/17.
 */
public class SuggestionDto {
    private static final long serialVersionUID = 1L;

    private Long userId;

    @NotNull
    private String subject;

    @NotNull
    private String content;

    @NotNull
    private String proposer = "Anonymous";

    @Email
    @NotNull
    private String email = "no-reply@indiepost.co.kr";

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
