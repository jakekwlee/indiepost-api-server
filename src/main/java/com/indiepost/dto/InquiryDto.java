package com.indiepost.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jake on 9/7/17.
 */
public class InquiryDto {

    private Long userId;

    @NotNull
    @Size(max = 50)
    private String inquirer;

    @NotNull
    @Email
    private String email;

    @Size(max = 50)
    private String contact;

    @Size(max = 50)
    private String clientName;

    private String url;

    @NotNull
    @Size(min = 1, max = 3000)
    private String content;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getInquirer() {
        return inquirer;
    }

    public void setInquirer(String inquirer) {
        this.inquirer = inquirer;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
