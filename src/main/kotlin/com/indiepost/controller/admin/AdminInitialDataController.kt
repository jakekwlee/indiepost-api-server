package com.indiepost.controller.admin

import com.indiepost.dto.AdminInitialResponse
import com.indiepost.service.AdminService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.inject.Inject

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping(value = ["/admin/init"], produces = ["application/json; charset=UTF-8"])
class AdminInitialDataController @Inject
constructor(private val adminService: AdminService) {

    @RequestMapping(method = [RequestMethod.GET])
    fun getInitialResponse(): AdminInitialResponse = adminService.buildInitialResponse()
}







