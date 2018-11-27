package com.indiepost.service

import com.indiepost.IndiepostBackendApplication
import com.indiepost.enums.Types
import com.indiepost.model.Profile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.time.LocalDateTime
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
@Transactional
internal class ProfileServiceTest {
    @Inject
    private lateinit var service: ProfileService

    private val insertedIds = ArrayList<Long>()

    @BeforeEach
    fun before() {
        val profile = Profile(slug = "test_slug")
        profile.created = LocalDateTime.now()
        profile.lastUpdated = LocalDateTime.now()
        profile.description = "Test description"
        profile.displayName = "Test User"
        profile.email = "sysadmin@indiepost.co.kr"
        profile.fullName = "Jake Lee"
        profile.label = "title"
        profile.profileState = Types.ProfileState.ACTIVE
        profile.profileType = Types.ProfileType.Editor
        profile.showEmail = true
        profile.showDescription = true
        profile.showLabel = true
        service.save(profile)
        insertedIds.add(profile.id!!)
    }

    @Test
    fun findOne_shouldFindEntityProperly() {
        val id = insertedIds[insertedIds.size - 1]
        assertThat(id).isNotNull()
        val profile = service.findOne(id)
        assertThat(profile!!).isNotNull()
    }

    @Test
    fun findOneBySlug_shouldWorkProperly() {
        val profile = service.findOneBySlug("test_slug")
        assertThat(profile!!).isNotNull()
    }

    @Test
    fun delete_shouldWorkProperly() {
        val profile = service.findOneBySlug("test_slug")
        assertThat(profile!!).isNotNull()
        val id = service.delete(profile)
        assertThat(id!!).isNotNull()
    }

    @Test
    fun count_shouldWorkProperly() {
        val n = service.count(Types.ProfileType.Editor)
        assertThat(n).isEqualTo(1)
    }

    @Test
    fun find() {
        val profiles = service.find(Types.ProfileType.Editor, PageRequest.of(0, 10))
        assertThat(profiles).isNotNull()
        assertThat(profiles).isNotEmpty()
        assertThat(profiles.totalElements).isEqualTo(1)
        assertThat(profiles.content.size).isEqualTo(1)
    }
}