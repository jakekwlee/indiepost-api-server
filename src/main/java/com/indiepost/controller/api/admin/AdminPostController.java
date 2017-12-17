package com.indiepost.controller.api.admin;

import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.FullTextSearchQuery;
import com.indiepost.service.AdminPostService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

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

    @PutMapping(value = "/{id}")
    public void update(@RequestBody AdminPostRequestDto adminPostRequestDto, @PathVariable Long id) {
        adminPostService.update(adminPostRequestDto);
    }

    @PostMapping(value = "/search")
    public List<AdminPostSummaryDto> search(@RequestBody FullTextSearchQuery query) {
        return null;

    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        adminPostService.deleteById(id);
    }

    @GetMapping
    public List<AdminPostSummaryDto> getPosts() {
        return adminPostService.find(0, 1000000, true);
    }

    @GetMapping(value = "/lastUpdated")
    public List<AdminPostSummaryDto> getLastUpdated() {
        return adminPostService.findLastUpdated(LocalDateTime.now().minusDays(1));
    }
}
