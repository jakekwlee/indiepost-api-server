package com.indiepost.enums;

/**
 * Created by jake on 9/7/16.
 */
public interface UserEnum {
    enum Gender {
        UNIDENTIFIED, FEMALE, MALE, ETC
    }

    enum Roles {
        User, Author, Editor, EditorInChief, Administrator
    }

    enum State {
        PENDING, ACTIVATED, DEACTIVATED, DELETED, BANNED, EXPIRED
    }

}
