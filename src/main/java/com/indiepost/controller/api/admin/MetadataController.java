package com.indiepost.controller.api.admin;

import com.indiepost.responseModel.admin.MetaInformation;
import com.indiepost.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping("/api/admin/metadata")
public class MetadataController {

    @Autowired
    private ManagementService managementService;

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public MetaInformation getMetadata() {
        return managementService.getMetaInformation();
    }
}







