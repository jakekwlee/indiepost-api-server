package com.indiepost.controller.api.admin;

import com.indiepost.service.AdminPostService;
import com.indiepost.dto.request.AdminPostRequestDto;
import com.indiepost.dto.response.AdminPostTableDto;
import com.indiepost.dto.response.AdminPostResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
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
    public AdminPostResponseDto getPost(@PathVariable Long id) {
        return adminPostService.getPostResponse(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public AdminPostResponseDto savePost(@RequestBody AdminPostRequestDto adminPostRequestDto) {
        return adminPostService.save(adminPostRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updatePost(@RequestBody AdminPostRequestDto adminPostRequestDto, @PathVariable Long id) {
        adminPostService.update(id, adminPostRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Long deletePost(@PathVariable Long id) {
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
    public List<AdminPostTableDto> getPostList() {
        return adminPostService.getAdminPostTableDtoList(0, 1000000, true);
    }

    @RequestMapping(value = "/lastUpdated", method = RequestMethod.GET)
    public List<AdminPostTableDto> getLastUpdated() {
        return adminPostService.getLastUpdated(getYesterday());
    }


    private Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal.getTime();
    }
}
