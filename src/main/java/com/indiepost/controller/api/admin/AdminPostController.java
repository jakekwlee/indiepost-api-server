package com.indiepost.controller.api.admin;

import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.enums.Types;
import com.indiepost.service.AdminPostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping(value = "/api/admin/posts", produces = {"application/json; charset=UTF-8"})
public class AdminPostController {

    private final AdminPostService adminPostService;

    @Inject
    public AdminPostController(AdminPostService adminPostService) {
        this.adminPostService = adminPostService;
    }

    @GetMapping(value = "/{id}")
    public AdminPostResponseDto get(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean edit) {
        if (edit) {
            return adminPostService.createAutosave(id);
        }
        return adminPostService.findOne(id);
    }

    @GetMapping
    public Page<AdminPostSummaryDto> getList(
            @RequestParam("status") String status,
            @RequestParam(value = "query", required = false) String query,
            Pageable pageable
    ) {
        Types.PostStatus postStatus = Types.PostStatus.valueOf(status.toUpperCase());
        if (StringUtils.isNotEmpty(query)) {
            return adminPostService.fullTextSearch(query, postStatus, pageable);
        }
        return adminPostService.find(postStatus, pageable);
    }

    @PutMapping(value = "/{id}")
    public void update(@RequestBody AdminPostRequestDto adminPostRequestDto, @PathVariable Long id) {
        adminPostService.update(adminPostRequestDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        adminPostService.deleteById(id);
    }
}
