package com.indiepost.controller.api.admin;

import com.indiepost.dto.CreateResponse;
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
    public AdminPostResponseDto get(@PathVariable Long id) {
        return adminPostService.findOne(id);
    }

    @PostMapping
    public CreateResponse createDraft(@RequestBody AdminPostRequestDto dto) {
        return adminPostService.createDraft(dto);
    }


    @PutMapping(value = "/{id}")
    public void update(@RequestBody AdminPostRequestDto dto, @PathVariable Long id) {
        if (!id.equals(dto.getOriginalId()) && !id.equals(dto.getId())) {
            // TODO throw error
            return;
        }
        adminPostService.update(dto);
    }

    @PostMapping(value = "/autosave")
    public CreateResponse createAutosave(@RequestBody AdminPostRequestDto dto) {
        return adminPostService.createAutosave(dto);
    }

    @PutMapping(value = "/autosave/{id}")
    public void updateAutosave(@RequestBody AdminPostRequestDto dto, @PathVariable Long id) {
        if (!id.equals(dto.getOriginalId()) && !id.equals(dto.getId())) {
            // TODO throw error
            return;
        }
        adminPostService.updateAutosave(dto);
    }

    @DeleteMapping(value = "/{id}")
    public DeleteResponse delete(@PathVariable Long id) {
        adminPostService.deleteById(id);
        return new DeleteResponse(id);
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
}
