package com.indiepost.enums

import com.fasterxml.jackson.annotation.JsonFormat

/**
 * Created by jake on 17. 4. 13.
 */
interface Types {

    enum class ImageSize {
        THUMBNAIL, SMALL, OPTIMIZED, LARGE, ORIGINAL
    }

    enum class PostStatus {
        AUTOSAVE, DRAFT, TRASH, PENDING, FUTURE, PUBLISH
    }

    enum class UserGender {
        UNIDENTIFIED, FEMALE, MALE, ETC
    }

    enum class UserState {
        PENDING, ACTIVATED, DEACTIVATED, DELETED, BANNED, EXPIRED
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    enum class UserRole {
        User, Author, Editor, EditorInChief, Administrator
    }

    enum class StatType {
        ACTION, POST, PAGE, HOME, SEARCH, TAG, PROFILE, CATEGORY, READING_HISTORY, BOOKMARK, NOTICE
    }

    enum class ActionType {
        LOAD_MORE_POSTS, CHANGE_THEME, OPEN_MENU,
        CLICK_APPSTORE, CLICK_SNS, CLICK_AD
    }

    enum class Channel {
        INBOUND_LINK, GOOGLE, TWITTER, FACEBOOK, INSTAGRAM, YOUTUBE, NAVER, BING, DAUM, NATE, OTHER, NONE
    }

    enum class ClientType {
        INDIEPOST_WEBAPP, INDIEPOST_LEGACY_MOBILE_APP, INDIEPOST_AD_ENGINE
    }

    enum class TimeDomainDuration {
        HOURLY, DAILY, MONTHLY, YEARLY
    }

    enum class ProfileType {
        Editor, Writer, Photographer
    }

    enum class ProfileState {
        ACTIVE, INACTIVE
    }

    enum class LinkType {
        Banner, Standard, FakeVideo
    }

    enum class BannerType {
        FullWidth, Grid, SidePanel
    }

    enum class BannerTarget {
        All, Users, NonUsers
    }

    companion object {

        fun isPublicStatus(status: PostStatus): Boolean {
            return (status == PostStatus.PUBLISH
                    || status == PostStatus.FUTURE
                    || status == PostStatus.PENDING)
        }
    }
}
