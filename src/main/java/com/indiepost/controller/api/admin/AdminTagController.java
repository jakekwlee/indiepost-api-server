package com.indiepost.controller.api.admin;

import com.indiepost.service.TagService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/admin/tag")
public class AdminTagController {

    @Inject
    private TagService tagService;


}
