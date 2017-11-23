package com.indiepost.controller.api.admin;

import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.service.AdminPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AdminPostResponseDto get(@PathVariable Long id) {
        return adminPostService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public AdminPostResponseDto save(@RequestBody AdminPostRequestDto adminPostRequestDto) {
        return adminPostService.save(adminPostRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody AdminPostRequestDto adminPostRequestDto, @PathVariable Long id) {
        adminPostService.update(id, adminPostRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Long delete(@PathVariable Long id) {
        adminPostService.deleteById(id);
        return id;
    }

    @RequestMapping(value = "/autosave", method = RequestMethod.POST)
    public AdminPostResponseDto createAutosave(@RequestBody AdminPostRequestDto adminPostRequestDto) {
        return adminPostService.createAutosave(adminPostRequestDto);
    }

    @RequestMapping(value = "/autosave/{id}", method = RequestMethod.PUT)
    public void updateAutosave(@PathVariable Long id, @RequestBody AdminPostRequestDto adminPostRequestDto) {
        adminPostService.updateAutosave(id, adminPostRequestDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AdminPostSummaryDto> getPosts() {
        return adminPostService.find(0, 1000000, true);
    }

    @RequestMapping(value = "/lastUpdated", method = RequestMethod.GET)
    public List<AdminPostSummaryDto> getLastUpdated() {
        return adminPostService.findLastUpdated(LocalDateTime.now().minusDays(1));
    }
}
