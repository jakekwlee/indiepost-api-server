package com.indiepost.controller.admin

import com.indiepost.dto.DeleteResponse
import com.indiepost.dto.StaticPageDto
import com.indiepost.dto.post.BulkStatusUpdateDto
import com.indiepost.enums.Types
import com.indiepost.service.StaticPageService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

import javax.inject.Inject
import javax.validation.ValidationException

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping(value = ["/admin/pages"], produces = ["application/json; charset=UTF-8"])
class AdminStaticPageController @Inject
constructor(private val staticPageService: StaticPageService) {

    @GetMapping
    fun getList(@RequestParam status: String, pageable: Pageable): Page<StaticPageDto> {
        return staticPageService.find(Types.PostStatus.valueOf(status.toUpperCase()), pageable)
    }

    @PostMapping
    fun savePage(@RequestBody staticPageDto: StaticPageDto): Long? {
        return staticPageService.save(staticPageDto)
    }

    @GetMapping("/{id}")
    fun getPage(@PathVariable id: Long): StaticPageDto? {
        return staticPageService.findById(id)
    }

    @PutMapping("/{id}")
    fun updatePage(@PathVariable id: Long, @RequestBody staticPageDto: StaticPageDto) {
        if (staticPageDto.id != id) {
            throw ValidationException("REST path variable ID and AdminPostRequestDto::id does not match.")
        }
        staticPageService.update(staticPageDto)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): DeleteResponse {
        staticPageService.deleteById(id)
        return DeleteResponse(id)
    }

    @PutMapping("/_bulk")
    fun bulkUpdate(@RequestBody dto: BulkStatusUpdateDto) {
        val ids = dto.ids
        if (ids == null || ids.isEmpty()) {
            throw ValidationException("BulkStatusUpdateDto::ids cannot be null or empty.")
        }
        val status = Types.PostStatus.valueOf(dto.status.toUpperCase())
        staticPageService.bulkUpdateStatus(ids, status)
    }

    @DeleteMapping("/_bulk")
    fun bulkDelete(@RequestBody dto: BulkStatusUpdateDto) {
        val ids = dto.ids
        if (ids == null || ids.isEmpty()) {
            throw ValidationException("BulkStatusUpdateDto::ids cannot be null or empty.")
        }
        staticPageService.bulkDeleteByIds(ids)
    }

    @DeleteMapping("/_trash")
    fun emptyTrash() {
        staticPageService.emptyTrash()
    }
}
