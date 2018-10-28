package com.indiepost.controller

import com.indiepost.service.StatsLoggerService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by jake on 8/7/17.
 */
@Controller
@RequestMapping("/link")
class LinkController @Inject
constructor(private val statsLoggerService: StatsLoggerService) {

    @GetMapping("/{uid}")
    fun logAndRedirect(@PathVariable uid: String,
                       req: HttpServletRequest,
                       res: HttpServletResponse,
                       principal: Principal): String {
        return "redirect:" + statsLoggerService.logClickAndGetLink(req, res, uid, principal)
    }
}
