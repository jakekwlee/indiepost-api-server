package com.indiepost.enums;

/**
 * Created by jake on 9/7/16.
 */
public interface IndiepostEnum {
    enum UserGender {
        UNIDENTIFIED, FEMALE, MALE, ETC
    }

    enum UserRoles {
        User, Author, Editor, EditorInChief, Administrator
    }

    enum UserState {
        PENDING, ACTIVATED, DEACTIVATED, DELETED, BANNED, EXPIRED
    }

    enum PostStatus {
        DELETED, DRAFT, RESERVED, PUBLISHED
    }

    enum PostType {
        POST, PAGE, NOTICE
    }

    enum ImageSizeType {
        Thumbnail, Small, Medium, Large, Xlarge, Original
    }
}
