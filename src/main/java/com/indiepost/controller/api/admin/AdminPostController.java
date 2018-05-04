package com.indiepost.controller.api.admin;

import com.indiepost.dto.DeleteResponse;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.BulkStatusUpdateDto;
import com.indiepost.enums.Types;
import com.indiepost.service.AdminPostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static com.indiepost.enums.Types.isPublicStatus;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping(value = "/api/admin/posts", produces = {"application/json; charset=UTF-8"})
public class AdminPostController {

    private final AdminPostService adminPostService;

    @Autowired
    public AdminPostController(AdminPostService adminPostService) {
        this.adminPostService = adminPostService;
    }

    @GetMapping(value = "/{id}")
    public AdminPostResponseDto get(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean edit) {
        AdminPostResponseDto post = adminPostService.findOne(id);
        if (edit) {
            Types.PostStatus postStatus = Types.PostStatus.valueOf(post.getStatus());
            if (isPublicStatus(postStatus)) {
                return adminPostService.createAutosaveFromPost(id);
            }
        }
        return post;
    }

    @PostMapping
    public AdminPostResponseDto createDraft(@RequestBody AdminPostRequestDto adminPostRequestDto) {
        return adminPostService.createDraft(adminPostRequestDto);
    }

    @PostMapping(value = "/_new")
    public AdminPostResponseDto createAutosave() {
        return adminPostService.createAutosave();
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
        if (!adminPostRequestDto.getId().equals(id)) {
            return;
        }
        adminPostService.update(adminPostRequestDto);
    }

    @PutMapping(value = "/_bulk")
    public void bulkUpdate(@RequestBody BulkStatusUpdateDto dto) {
        Types.PostStatus status = Types.PostStatus.valueOf(dto.getStatus().toUpperCase());
        adminPostService.bulkStatusUpdate(dto.getIds(), status);
    }

    @DeleteMapping(value = "/_bulk")
    public void bulkDelete(@RequestBody BulkStatusUpdateDto bulkStatusUpdateDto) {
        adminPostService.bulkDeleteByIds(bulkStatusUpdateDto.getIds());
    }

    @DeleteMapping(value = "/_trash")
    public void emptyTrash() {
        adminPostService.bulkDeleteByStatus(Types.PostStatus.TRASH);
    }

    @DeleteMapping(value = "/{id}")
    public DeleteResponse delete(@PathVariable Long id) {
        adminPostService.deleteById(id);
        return new DeleteResponse(id);
    }
}
