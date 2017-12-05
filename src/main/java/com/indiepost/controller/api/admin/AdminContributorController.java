package com.indiepost.controller.api.admin;

import com.indiepost.dto.ContributorDto;
import com.indiepost.service.ContributorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/api/admin/contributor", produces = {"application/json; charset=UTF-8"})
public class AdminContributorController {

    private final ContributorService contributorService;

    @Inject
    public AdminContributorController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @PostMapping
    public ContributorDto createNewContributor(@RequestBody ContributorDto contributorDto) {
        return contributorService.save(contributorDto);
    }
}
