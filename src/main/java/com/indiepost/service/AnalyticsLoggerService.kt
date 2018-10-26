package com.indiepost.service

/**
 * Created by jake on 17. 5. 25.
 */

import com.indiepost.dto.analytics.ActionDto
import com.indiepost.dto.analytics.PageviewDto
import com.indiepost.model.analytics.Visitor
import java.io.IOException
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface AnalyticsLoggerService {
    fun findOne(id: Long?): Visitor?

    @Throws(IOException::class)
    fun logPageview(req: HttpServletRequest, res: HttpServletResponse, pageviewDto: PageviewDto)

    @Throws(IOException::class)
    fun logAction(req: HttpServletRequest, res: HttpServletResponse, actionDto: ActionDto)

    @Throws(IOException::class)
    fun logClickAndGetLink(req: HttpServletRequest, res: HttpServletResponse, principal: Principal?, linkUid: String?): String
}
