package com.indiepost.controller.admin

import com.indiepost.dto.LinkMetadataResponse
import com.indiepost.dto.analytics.LinkDto
import com.indiepost.service.LinkService
import org.springframework.web.bind.annotation.*
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.ValidationException

/**
 * Created by jake on 8/10/17.
 */
@RestController
@RequestMapping(value = ["/admin/link"], produces = ["application/json; charset=UTF-8"])
class AdminLinkController @Inject
constructor(private val linkService: LinkService) {

    @GetMapping("/{id}")
    fun getLink(@PathVariable id: Long): LinkDto? {
        return linkService.findById(id)
    }

    @PostMapping
    fun createLink(@Valid @RequestBody linkDto: LinkDto): LinkDto {
        return linkService.save(linkDto)
    }

    @GetMapping("/search/movie")
    fun searchMovies(@RequestParam text: String, @RequestParam size: Int): List<LinkMetadataResponse> {
        if (text.isEmpty())
            return emptyList()
        if (text.contains("http"))
            return linkService.getLinkMetadata(text)
        return linkService.searchMovies(text, size)
    }

    @PutMapping("/{id}")
    fun updateLink(@PathVariable id: Long, @Valid @RequestBody linkDto: LinkDto) {
        if (id != linkDto.id) {
            throw ValidationException("REST resource ID and LinkDto::id are not match.")
        }
        linkService.update(linkDto)
    }

    @DeleteMapping("/{id}")
    fun deleteLink(@PathVariable id: Long) {
        linkService.deleteById(id)
    }
}
