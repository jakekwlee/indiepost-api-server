package com.indiepost.controller

import com.indiepost.dto.analytics.ActionDto
import com.indiepost.dto.analytics.PageviewDto
import com.indiepost.service.PostUserInteractionService
import com.indiepost.service.StatsLoggerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping("/stat")
class StatsLoggerController @Inject
constructor(private val statsLoggerService: StatsLoggerService, private val postUserInteractionService: PostUserInteractionService) {

    @GetMapping("/pageview")
    fun logPageview(request: HttpServletRequest,
                    response: HttpServletResponse,
                    @RequestParam(name = "n") appName: String,
                    @RequestParam(name = "v") appVersion: String,
                    @RequestParam(name = "h") path: String,
                    @RequestParam(name = "p", required = false) postId: Long?,
                    @RequestParam(name = "r", required = false) referrer: String?,
                    @RequestParam(name = "u", required = false) userId: Long?
    ) {
        val dto = PageviewDto()
        dto.appName = appName
        dto.appVersion = appVersion
        dto.referrer = referrer
        dto.postId = postId
        dto.path = path
        dto.userId = userId
        statsLoggerService.logPageview(request, response, dto)

        if (userId != null && postId != null) {
            postUserInteractionService.add(userId, postId)
        }
    }


    @GetMapping("/action")
    fun logAction(request: HttpServletRequest,
                  response: HttpServletResponse,
                  @RequestParam(name = "n") appName: String,
                  @RequestParam(name = "v") appVersion: String,
                  @RequestParam(name = "a") actionType: String,
                  @RequestParam(name = "h") path: String,
                  @RequestParam(name = "i", required = false) value: Int?,
                  @RequestParam(name = "l", required = false) label: String?,
                  @RequestParam(name = "u", required = false) userId: Long?
    ) {
        val dto = ActionDto(appName, appVersion, path, actionType, label, value, userId)
        statsLoggerService.logAction(request, response, dto)
    }
}
