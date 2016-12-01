package com.indiepost.enums;

/**
 * Created by jake on 16. 9. 8.
 */
public interface PostEnum {
    enum Status {
        AUTOSAVE, DRAFT, PENDING, FUTURE, PUBLISH, TRASH
    }

    enum Type {
        POST, PAGE, NOTICE
    }
}