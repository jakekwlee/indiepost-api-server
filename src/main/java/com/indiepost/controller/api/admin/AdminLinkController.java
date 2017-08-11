package com.indiepost.controller.api.admin;

import com.indiepost.dto.LinkDto;
import com.indiepost.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;

/**
 * Created by jake on 8/10/17.
 */
@RestController
@RequestMapping(value = "/api/admin/link", produces = {"application/json; charset=UTF-8"})
public class AdminLinkController {

    private final LinkService linkService;

    @Autowired
    public AdminLinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    public List<LinkDto> getLinks() {
        return linkService.findAll();
    }

    @GetMapping(value = "/{id}")
    public LinkDto getLink(@PathVariable Long id) {
        return linkService.findById(id);
    }

    @PostMapping
    public LinkDto createLink(@Valid LinkDto linkDto) {
        return linkService.save(linkDto);
    }

    @PutMapping(value = "/{id}")
    public void updateLink(@PathVariable Long id, @Valid LinkDto linkDto) {
        if (!Objects.equals(id, linkDto.getId())) {
            throw new ValidationException("REST resource ID and LinkDto::id are not match.");
        }
        linkService.update(linkDto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteLink(@PathVariable Long id) {
        linkService.deleteById(id);
    }
}
