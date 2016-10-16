package com.indiepost.enums;

/**
 * Created by jake on 16. 9. 8.
 */
public interface PostEnum {
    enum Status {
        DELETED, AUTOSAVED, DRAFT, QUEUED, BOOKED, PUBLISHED
    }

    enum Type {
        POST, PAGE, NOTICE
    }
}