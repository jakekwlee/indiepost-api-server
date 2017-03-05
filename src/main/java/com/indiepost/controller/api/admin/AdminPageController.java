package com.indiepost.controller.api.admin;

import com.indiepost.dto.PageDto;
import com.indiepost.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping(value = "/api/admin/pages", produces = {"application/json; charset=UTF-8"})
public class AdminPageController {

    private final PageService pageService;

    @Autowired
    public AdminPageController(PageService pageService) {
        this.pageService = pageService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PageDto> getList() {
        return pageService.find(0, 1000, false);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Long savePage(@RequestBody PageDto pageDto) {
        return pageService.save(pageDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PageDto getPage(@PathVariable Long id) {
        return pageService.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updatePage(@PathVariable Long id, @RequestBody PageDto pageDto) {
        if (!pageDto.getId().equals(id)) {
            return;
        }
        pageService.update(pageDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Long delete(@PathVariable Long id) {
        pageService.deleteById(id);
        return id;
    }

}
