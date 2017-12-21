package com.indiepost.controller.api.admin;

import com.indiepost.dto.TagDto;
import com.indiepost.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/tag", produces = {"application/json; charset=UTF-8"})
public class AdminTagController {

    private final TagService tagService;

    @Inject
    public AdminTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public TagDto createNewTag(@RequestBody TagDto tagDto) {
        return tagService.save(tagDto);
    }

    @GetMapping
    public List<TagDto> searchTags(@RequestParam String tagName) {
        return tagService.findByNameLike(tagName);
    }
}
