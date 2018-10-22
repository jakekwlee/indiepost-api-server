package com.indiepost.dto.post

import com.indiepost.enums.Types

data class BulkStatusUpdateDto(
        var ids: List<Long>? = null,

        var status: String = Types.PostStatus.DRAFT.toString()
)
