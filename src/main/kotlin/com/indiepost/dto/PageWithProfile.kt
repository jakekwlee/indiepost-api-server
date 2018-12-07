package com.indiepost.dto

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class PageWithProfile<T> : PageImpl<T> {

    val profile: ProfileDto?

    constructor() : super(emptyList<T>()) {
        this.profile = null
    }

    constructor(profile: ProfileDto, content: List<T>, pageable: Pageable, total: Long) : super(content, pageable, total) {
        this.profile = profile
    }
}
