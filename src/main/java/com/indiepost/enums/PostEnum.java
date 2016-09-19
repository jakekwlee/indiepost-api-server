package com.indiepost.enums;

/**
 * Created by jake on 16. 9. 8.
 */
public interface PostEnum {
    enum Status {
        DRAFT, QUEUED, RESERVED, PUBLISHED, DELETED
    }

    enum Type {
        POST, PAGE, NOTICE
    }
}