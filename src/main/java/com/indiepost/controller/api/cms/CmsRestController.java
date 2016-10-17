package com.indiepost.controller.api.cms;

import com.indiepost.service.ManagementService;
import com.indiepost.viewModel.cms.AdminInitialResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping("/api/cms")
public class CmsRestController {

    @Autowired
    ManagementService managementService;

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public AdminInitialResponse getInitialData() {
        return managementService.getInitialState();
    }

}
