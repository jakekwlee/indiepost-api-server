package com.indiepost.controller.api.management;

import com.indiepost.service.ManagementService;
import com.indiepost.viewModel.cms.TopLevelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping("/api/management")
public class ManagementRestController {

    @Autowired
    ManagementService managementService;

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public TopLevelResponse getInitialData() {
        return managementService.getInitialState();
    }

}
