package com.indiepost.controller.api.admin;

import com.indiepost.dto.ContributorDto;
import com.indiepost.dto.DeletedResponse;
import com.indiepost.enums.Types;
import com.indiepost.service.ContributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/contributors")
public class AdminContributorController {

    @Autowired
    private ContributorService contributorService;

    @GetMapping
    public Page<ContributorDto> get(@RequestParam String type, @RequestParam Pageable pageable) {
        return contributorService.find(Types.ContributorType.valueOf(type), pageable);
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
    public DeletedResponse delete(@PathVariable Long id) {
        Long deletedId = contributorService.deleteById(id);
        return new DeletedResponse(deletedId);
    }

}
