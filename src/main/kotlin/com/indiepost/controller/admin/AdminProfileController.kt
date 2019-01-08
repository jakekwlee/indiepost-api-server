package com.indiepost.controller.admin

import com.indiepost.dto.CreateResponse
import com.indiepost.dto.DeleteResponse
import com.indiepost.dto.ProfileDto
import com.indiepost.enums.Types
import com.indiepost.service.PostMigrationService
import com.indiepost.service.ProfileService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import javax.inject.Inject
import javax.validation.ValidationException

@RestController
@RequestMapping("/admin/profiles")
class AdminProfileController @Inject
constructor(private val profileService: ProfileService, private val postMigrationService: PostMigrationService) {

    @GetMapping
    fun getList(@RequestParam type: String?, pageable: Pageable): Page<ProfileDto> {
        return if (type != null) {
            profileService.find(Types.ProfileType.valueOf(type), pageable)
        } else profileService.find(pageable)
    }

    @GetMapping("/{id}")
    fun getProfile(@PathVariable id: Long): ProfileDto? {
        return profileService.findOne(id)
    }

    @GetMapping("/ids")
    fun getProfiles(@RequestParam ids: List<Long>): List<ProfileDto> {
        return profileService.findByIdIn(ids)
    }

    @PostMapping
    fun create(@RequestBody dto: ProfileDto): CreateResponse {
        val id = profileService.save(dto)
        return CreateResponse(id)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: ProfileDto) {
        if (dto.id != id) {
            throw ValidationException("Path variable and dto.id does not match: path($id) != dto(${dto.id})")
        }
        profileService.save(dto)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): DeleteResponse {
        val deletedId = profileService.deleteById(id)
        return DeleteResponse(deletedId!!)
    }
}
