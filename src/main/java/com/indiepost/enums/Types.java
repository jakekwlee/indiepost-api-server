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
        AUTOSAVE, DRAFT, TRASH, DELETED, PENDING, FUTURE, PUBLISH
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

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    enum ContributorRole {
        FeatureEditor, FreeranceEditor, Photographer
    }

    enum StatType {
        ACTION, POST, PAGE, HOME, SEARCH, TAG, CATEGORY, NOTICE
    }

    enum ActionType {
        LOAD_MORE_POSTS, CHANGE_THEME, OPEN_MENU,
        CLICK_APPSTORE, CLICK_SNS, CLICK_AD
    }

    enum Channel {
        INBOUND_LINK, GOOGLE, TWITTER, FACEBOOK, INSTAGRAM, YOUTUBE, NAVER, BING, DAUM, NATE, OTHER, NONE
    }

    enum ClientType {
        INDIEPOST_WEBAPP, INDIEPOST_LEGACY_MOBILE_APP, INDIEPOST_AD_ENGINE
    }

    enum TimeDomainDuration {
        HOURLY, DAILY, MONTHLY, YEARLY
    }
}
