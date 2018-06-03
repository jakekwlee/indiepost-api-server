package com.indiepost.controller.admin;

import com.indiepost.dto.ContributorDto;
import com.indiepost.dto.DeleteResponse;
import com.indiepost.enums.Types;
import com.indiepost.service.ContributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/contributors")
public class AdminContributorController {

    private final ContributorService contributorService;

    @Autowired
    public AdminContributorController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @GetMapping
    public Page<ContributorDto> get(@RequestParam String type, Pageable pageable) {
        if (type != null) {
            return contributorService.find(Types.ContributorType.valueOf(type), pageable);
        }
        return contributorService.find(pageable);
    }

    @GetMapping("/{id}")
    public ContributorDto get(@PathVariable Long id) {
        return contributorService.findOne(id);
    }

    @PostMapping
    public ContributorDto create(@RequestBody ContributorDto dto) {
        return contributorService.save(dto);
    }

    @PutMapping("/{id}")
    public ContributorDto update(@PathVariable Long id, @RequestBody ContributorDto dto) {
        if (dto.getId() == null || !dto.getId().equals(id)) {
            dto.setId(id);
        }
        return contributorService.save(dto);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse delete(@PathVariable Long id) {
        Long deletedId = contributorService.deleteById(id);
        return new DeleteResponse(deletedId);
    }

}
