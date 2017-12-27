package com.indiepost.controller.api.admin;

import com.indiepost.dto.ContributorDto;
import com.indiepost.service.ContributorService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/contributor", produces = {"application/json; charset=UTF-8"})
public class AdminContributorController {

    private final ContributorService contributorService;

    @Inject
    public AdminContributorController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @GetMapping(value = "/{id}")
    public ContributorDto get(@PathVariable Long id) {
        return contributorService.findById(id);
    }

    @PostMapping
    public ContributorDto create(@RequestBody ContributorDto contributorDto) {
        return contributorService.save(contributorDto);
    }

    @PutMapping(value = "/{id}")
    public void update(@PathVariable Long id, @RequestBody ContributorDto contributorDto) {
        contributorDto.setId(id);
        contributorService.update(contributorDto);
    }

    //TODO GET contributor list pagination
    @GetMapping
    public List<ContributorDto> getList(
            @RequestParam("p") int page,
            @RequestParam("m") int maxResults
    ) {
        return contributorService.findAll();
    }
}
