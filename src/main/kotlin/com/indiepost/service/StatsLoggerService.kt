package com.indiepost.service

/**
 * Created by jake on 17. 5. 25.
 */

import com.indiepost.dto.analytics.ActionDto
import com.indiepost.dto.analytics.PageviewDto
import com.indiepost.model.analytics.Visitor
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface StatsLoggerService {
    fun findOne(id: Long): Visitor?

    fun logPageview(req: HttpServletRequest, res: HttpServletResponse, pageviewDto: PageviewDto)

    fun logAction(req: HttpServletRequest, res: HttpServletResponse, actionDto: ActionDto)

    fun logClickAndGetLink(req: HttpServletRequest, res: HttpServletResponse,
                           linkUid: String, principal: Principal?): String
}
