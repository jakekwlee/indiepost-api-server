package com.indiepost.service

import com.indiepost.dto.analytics.ActionDto
import com.indiepost.dto.analytics.PageviewDto
import com.indiepost.enums.Types.Channel
import com.indiepost.enums.Types.ClientType
import com.indiepost.exceptions.UnauthorizedException
import com.indiepost.model.UserAgent
import com.indiepost.model.analytics.Action
import com.indiepost.model.analytics.Click
import com.indiepost.model.analytics.Pageview
import com.indiepost.model.analytics.Visitor
import com.indiepost.repository.*
import org.apache.commons.lang3.StringUtils.isNotEmpty
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils.isEmpty
import java.io.IOException
import java.security.Principal
import java.time.LocalDateTime
import javax.inject.Inject
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional

/**
 * Created by jake on 17. 5. 25.
 */
@Service
@Transactional
class AnalyticsLoggerServiceImpl @Inject constructor(
        private val visitorRepository: VisitorRepository,
        private val pageviewRepository: PageviewRepository,
        private val actionRepository: ActionRepository,
        private val userService: UserService,
        private val linkRepository: LinkRepository,
        private val clickRepository: ClickRepository) : AnalyticsLoggerService {

    @Value("\${spring.profiles.active}")
    private val activeProfile: String? = null

    override fun findOne(id: Long): Visitor? {
        return visitorRepository.findOne(id)
    }

    @Throws(IOException::class)
    override fun logPageview(req: HttpServletRequest, res: HttpServletResponse, pageviewDto: PageviewDto) {
        var visitorId = getVisitorId(req, pageviewDto.userId)
        val pageview = Pageview()

        if (visitorId == null) {
            val visitor = newVisitor(req, res, pageviewDto.userId, pageviewDto.appName, pageviewDto.appVersion)
                    ?: return
            visitorId = visitor.id
            pageview.isLandingPage = true

            if (isNotEmpty(pageviewDto.referrer)) {
                var referrer = pageviewDto.referrer
                if (referrer!!.length > 1 && referrer.endsWith("/")) {
                    referrer = referrer.substring(0, referrer.length - 1)
                }
                visitor.referrer = referrer
            }
            val channel = getChannelType(visitor.browser!!, pageviewDto.referrer)
            visitor.channel = channel
        }
        pageview.visitorId = visitorId
        pageview.path = pageviewDto.path
        if (pageviewDto.postId != null) {
            pageview.postId = pageviewDto.postId
        }
        pageview.timestamp = LocalDateTime.now()
        pageviewRepository.save(pageview)
    }

    @Throws(IOException::class)
    override fun logAction(req: HttpServletRequest, res: HttpServletResponse, actionDto: ActionDto) {
        var visitorId = getVisitorId(req, actionDto.userId)
        if (visitorId == null) {
            val (id) = newVisitor(req, res, actionDto.userId, actionDto.appName, actionDto.appVersion) ?: return
            visitorId = id
        }
        val action = Action()
        action.visitorId = visitorId
        action.path = actionDto.path
        action.actionType = actionDto.actionType
        if (isNotEmpty(actionDto.label)) {
            action.label = actionDto.label
        }
        action.value = actionDto.value
        action.timestamp = LocalDateTime.now()
        actionRepository.save(action)
    }

    @Throws(IOException::class)
    override fun logClickAndGetLink(req: HttpServletRequest, res: HttpServletResponse, linkUid: String, principal: Principal?): String {
        if (linkUid.isEmpty()) {
            return "/"
        }
        val link = linkRepository.findByUid(linkUid)
        var userId: Long? = null
        if (principal != null) {
            val user = userService.findCurrentUser() ?: throw UnauthorizedException()
            userId = user.id
        }
        var visitorId = getVisitorId(req, userId)
        if (visitorId == null) {
            val (id) = newVisitor(req, res, userId, ClientType.INDIEPOST_AD_ENGINE.toString(), "0.9.0")
                    ?: return link.url!!
            visitorId = id
        }
        val click = Click()
        click.visitorId = visitorId
        click.path = req.requestURI
        click.link = link
        click.timestamp = LocalDateTime.now()
        clickRepository.save(click)
        return link.url!!
    }

    @Throws(IOException::class)
    private fun newVisitor(req: HttpServletRequest, res: HttpServletResponse, userId: Long?, appName: String?, appVersion: String?): Visitor? {
        val userAgentString = req.getHeader("User-Agent")
        val ipAddress = getIpAddress(req)

        val visitor = Visitor()
        val parser = ua_parser.Parser()
        val ua = parser.parse(userAgentString)

        val browserName = ua.userAgent.family
        val osName = ua.os.family
        val deviceName = ua.device.family

        if (isNotEmpty(deviceName)) {
            if (deviceName.contains("Spider") ||
                    browserName.contains(Regex("python|yeti", RegexOption.IGNORE_CASE))) {
                logger.info("A visitor is filtered by blacklist, skip DB insert: {} : {} : {}",
                        browserName, ipAddress, req.requestURI)
                return null
            }
            visitor.browser = browserName
            visitor.browserVersion = getBrowserVersion(ua.userAgent)
        } else {
            val userAgent = UserAgent()
            userAgent.uaString = userAgentString
            userAgent.setUaHash(userAgentString)
            visitor.userAgent = userAgent
        }

        if (isNotEmpty(osName)) {
            visitor.os = osName
            visitor.osVersion = this.getOsVersion(ua.os)
        }

        if (isNotEmpty(deviceName)) {
            visitor.device = deviceName
        }

        if (userId != null) {
            visitor.userId = userId
        }

        if (ClientType.INDIEPOST_AD_ENGINE.toString() == appName) {
            visitor.isAdVisitor = true
        }
        visitor.appName = appName
        visitor.appVersion = appVersion
        visitor.ipAddress = ipAddress
        visitor.timestamp = LocalDateTime.now()

        val visitorId = visitorRepository.save(visitor)

        val cookie = Cookie("visitorId", visitorId!!.toString())
        cookie.maxAge = 1800 // expires after 30min

        if (activeProfile == "prod") {
            cookie.domain = "indiepost.co.kr"
        }
        res.addCookie(cookie)
        return visitor
    }

    private fun getVisitorId(request: HttpServletRequest, userId: Long?): Long? {
        val cookieArray = request.cookies ?: return null

        var visitorId: Long? = null
        for (cookie in cookieArray) {
            if (cookie.name == "visitorId") {
                visitorId = java.lang.Long.parseLong(cookie.value)
                break
            }
        }

        if (visitorId == null) {
            return null
        }

        if (userId != null) {
            val visitor = this.findOne(visitorId)
            if (visitor != null && visitor.userId == null) {
                visitor.userId = userId
                visitorRepository.save(visitor)
            }
        }
        return visitorId
    }

    private fun getIpAddress(req: HttpServletRequest): String {
        val ip = req.getHeader("X-FORWARDED-FOR")
        return ip ?: req.remoteAddr
    }

    private fun getChannelType(browserName: String, referrer: String?): Channel {
        if (browserName.toLowerCase().contains("facebook")) {
            return Channel.FACEBOOK
        }
        if (isEmpty(referrer)) {
            return Channel.NONE
        }
        val domain = referrer!!.split("//".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

        if (domain.contains("indiepost.co.kr")) {
            return Channel.INBOUND_LINK
        }
        if (domain.contains("goo")) {
            return Channel.GOOGLE
        }
        if (domain.contains("facebook") || domain.contains("fb")) {
            return Channel.FACEBOOK
        }
        if (domain.contains("t.co")) {
            return Channel.TWITTER
        }
        if (domain.contains("naver")) {
            return Channel.NAVER
        }
        if (domain.contains("daum")) {
            return Channel.DAUM
        }
        return if (domain.contains("instagram")) {
            Channel.INSTAGRAM
        } else Channel.OTHER

    }

    private fun getBrowserVersion(userAgent: ua_parser.UserAgent): String? {
        val stringBuilder = StringBuilder()
        if (userAgent.major != null) {
            stringBuilder.append(userAgent.major)
            if (userAgent.minor != null) {
                stringBuilder.append('.')
                stringBuilder.append(userAgent.minor)
                if (userAgent.patch != null) {
                    stringBuilder.append('.')
                    stringBuilder.append(userAgent.patch)
                }
            }
            return stringBuilder.toString()
        }
        return null
    }

    private fun getOsVersion(os: ua_parser.OS): String? {
        val sb = StringBuilder()
        if (os.major != null) {
            sb.append(os.major)
            if (os.minor != null) {
                sb.append('.')
                sb.append(os.minor)
                if (os.patch != null) {
                    sb.append('.')
                    sb.append(os.patch)
                    if (os.patchMinor != null) {
                        sb.append('.')
                        sb.append(os.patchMinor)
                    }
                }
            }
            return sb.toString()
        }
        return null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AnalyticsLoggerServiceImpl::class.java)
    }
}
