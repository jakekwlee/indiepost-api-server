package com.indiepost.enums;

/**
 * Created by jake on 16. 9. 8.
 */
public interface PostEnum {
    enum Status {
        DELETED, DRAFT, RESERVED, PUBLISHED
    }

    enum Type {
        POST, PAGE, NOTICE
    }
}