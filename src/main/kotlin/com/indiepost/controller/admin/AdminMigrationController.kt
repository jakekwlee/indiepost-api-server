package com.indiepost.controller.admin

import com.indiepost.service.PostMigrationService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/admin/migration")
class AdminMigrationController @Inject constructor(private val service: PostMigrationService) {

    companion object {
        private val logger = LoggerFactory.getLogger(AdminMigrationController::class.java)
    }

    @GetMapping("/20190112")
    fun migrate(request: HttpServletRequest, principal: Principal) {
        val ipAddress = request.getHeader("X-FORWARDED-FOR") ?: request.remoteAddr
        val username = principal.name
        logger.info("'Migrate Categories to Tags' has been triggered: $username ($ipAddress)")
//        service.migrateCategoriesToTags()
        logger.info("Migration complete")
    }
}
