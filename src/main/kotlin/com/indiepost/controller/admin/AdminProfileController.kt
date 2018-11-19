package com.indiepost.controller.admin

import com.indiepost.dto.DeleteResponse
import com.indiepost.enums.Types
import com.indiepost.model.Profile
import com.indiepost.service.ProfileService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import javax.inject.Inject
import javax.validation.ValidationException

@RestController
@RequestMapping("/admin/profiles")
class AdminProfileController @Inject
constructor(private val profileService: ProfileService) {

    @GetMapping
    fun getList(@RequestParam type: String?, pageable: Pageable): Page<Profile> {
        return if (type != null) {
            profileService.find(Types.ProfileType.valueOf(type), pageable)
        } else profileService.find(pageable)
    }

    @GetMapping("/{id}")
    fun getProfile(@PathVariable id: Long): Profile? {
        return profileService.findOne(id)
    }

    @PostMapping
    fun create(@RequestBody profile: Profile): Profile {
        return profileService.save(profile)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody profile: Profile): Profile {
        if (profile.id != id) {
            throw ValidationException("Path variable and dto.id does not match: path($id) != dto(${profile.id})")
        }
        return profileService.save(profile)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): DeleteResponse {
        val deletedId = profileService.deleteById(id)
        return DeleteResponse(deletedId!!)
    }
}
