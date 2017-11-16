package com.indiepost.controller.api.admin;

import com.indiepost.dto.AdminInitialData;
import com.indiepost.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping(value = "/api/admin/init", produces = {"application/json; charset=UTF-8"})
public class AdminInitialDataController {

    private final AdminService adminService;

    @Autowired
    public AdminInitialDataController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public AdminInitialData getInitialResponse() {
        return adminService.buildInitialResponse();
    }
}







