package com.indiepost.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by jake on 17. 4. 13.
 */
public interface Types {
    enum ImageSize {
        THUMBNAIL, SMALL, OPTIMIZED, LARGE, ORIGINAL
    }

    enum PostStatus {
        AUTOSAVE, DRAFT, PENDING, FUTURE, PUBLISH, TRASH
    }

    enum UserGender {
        UNIDENTIFIED, FEMALE, MALE, ETC
    }

    enum UserState {
        PENDING, ACTIVATED, DEACTIVATED, DELETED, BANNED, EXPIRED
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    enum UserRole {
        User, Author, Editor, EditorInChief, Administrator
    }

    enum StatType {
        ACTION, POST, PAGE, HOME, SEARCH, TAG, CATEGORY, NOTICE
    }

    enum ActionType {
        LOAD_MORE_POSTS, CHANGE_THEME, OPEN_MENU,
        CLICK_APPSTORE, CLICK_SNS, CLICK_AD
    }
}
