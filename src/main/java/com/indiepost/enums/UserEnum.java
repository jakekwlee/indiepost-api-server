package com.indiepost.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by jake on 9/7/16.
 */
public interface UserEnum {
    enum Gender {
        UNIDENTIFIED, FEMALE, MALE, ETC
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    enum Roles {
        User, Author, Editor, EditorInChief, Administrator
    }

    enum State {
        PENDING, ACTIVATED, DEACTIVATED, DELETED, BANNED, EXPIRED
    }
}
