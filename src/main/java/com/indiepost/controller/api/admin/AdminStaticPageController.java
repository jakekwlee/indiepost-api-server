package com.indiepost.controller.api.admin;

import com.indiepost.dto.DeleteResponse;
import com.indiepost.dto.StaticPageDto;
import com.indiepost.enums.Types;
import com.indiepost.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping(value = "/api/admin/pages", produces = {"application/json; charset=UTF-8"})
public class AdminStaticPageController {

    private final StaticPageService staticPageService;

    @Autowired
    public AdminStaticPageController(StaticPageService staticPageService) {
        this.staticPageService = staticPageService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<StaticPageDto> getList(@RequestParam String status, Pageable pageable) {
        return staticPageService.find(Types.PostStatus.valueOf(status.toUpperCase()), pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Long savePage(@RequestBody StaticPageDto staticPageDto) {
        return staticPageService.save(staticPageDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public StaticPageDto getPage(@PathVariable Long id) {
        return staticPageService.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updatePage(@PathVariable Long id, @RequestBody StaticPageDto staticPageDto) {
        if (!staticPageDto.getId().equals(id)) {
            return;
        }
        staticPageService.update(staticPageDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public DeleteResponse delete(@PathVariable Long id) {
        staticPageService.deleteById(id);
        return new DeleteResponse(id);
    }

}
