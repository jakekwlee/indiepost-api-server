package com.indiepost.service

import com.indiepost.IndiepostBackendApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@Transactional
@WebAppConfiguration
internal class PostMigrationServiceImplTest {
    @Inject
    private lateinit var service: PostMigrationService

    @Test
    fun migratePostProfile_shouldWorkAsExpected() {
        service.migrateProfiles()
    }

    @Test
    fun findProfileByEtc_shouldReturnProfileProperly() {
        val profile = service.findProfileByEtc("ari song")
        assertThat(profile).isNotNull()

    }
}