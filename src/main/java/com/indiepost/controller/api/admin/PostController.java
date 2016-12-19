package com.indiepost.controller.api.admin;

import com.indiepost.service.AdminService;
import com.indiepost.service.PostService;
import dto.request.AdminPostRequestDto;
import dto.response.AdminDataTableItem;
import dto.response.AdminPostResponseDto;
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
public class PostController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PostService postService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AdminPostResponseDto getPost(@PathVariable Long id) {
        return postService.getPostResponse(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public AdminPostResponseDto savePost(@RequestBody AdminPostRequestDto adminPostRequestDto) {
        return postService.save(adminPostRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updatePost(@RequestBody AdminPostRequestDto adminPostRequestDto, @PathVariable Long id) {
        postService.update(id, adminPostRequestDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Long deletePost(@PathVariable Long id) {
        postService.deleteById(id);
        return id;
    }

    @RequestMapping(value = "/autosave", method = RequestMethod.POST)
    public AdminPostResponseDto createAutosave(@RequestBody AdminPostRequestDto adminPostRequestDto) {
        return postService.createAutosave(adminPostRequestDto);
    }

    @RequestMapping(value = "/autosave/{id}", method = RequestMethod.PUT)
    public void updateAutosave(@PathVariable Long id, @RequestBody AdminPostRequestDto adminPostRequestDto) {
        postService.updateAutosave(id, adminPostRequestDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AdminDataTableItem> getPostList() {
        return adminService.getAdminPostListItemDtos(0, 1000000, true);
    }

    @RequestMapping(value = "/lastUpdated", method = RequestMethod.GET)
    public List<AdminDataTableItem> getLastUpdated() {
        return adminService.getLastUpdated(getYesterday());
    }


    private Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date oneDayBefore = cal.getTime();
        return oneDayBefore;
    }
}
