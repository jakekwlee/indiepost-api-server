package com.indiepost.controller.admin

import com.indiepost.dto.ContributorDto
import com.indiepost.dto.DeleteResponse
import com.indiepost.enums.Types
import com.indiepost.service.ContributorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import javax.inject.Inject
import javax.validation.ValidationException

@RestController
@RequestMapping("/admin/contributors")
class AdminContributorController @Inject
constructor(private val contributorService: ContributorService) {

    @GetMapping
    fun getList(@RequestParam type: String?, pageable: Pageable): Page<ContributorDto> {
        return if (type != null) {
            contributorService.find(Types.ContributorType.valueOf(type), pageable)
        } else contributorService.find(pageable)
    }

    @GetMapping("/{id}")
    fun getContributor(@PathVariable id: Long): ContributorDto? {
        return contributorService.findOne(id)
    }

    @PostMapping
    fun create(@RequestBody dto: ContributorDto): ContributorDto {
        return contributorService.save(dto)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: ContributorDto): ContributorDto {
        if (dto.id != id) {
            throw ValidationException("Path variable and dto.id does not match: path($id) != dto(${dto.id})")
        }
        return contributorService.save(dto)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): DeleteResponse {
        val deletedId = contributorService.deleteById(id)
        return DeleteResponse(deletedId!!)
    }

}
