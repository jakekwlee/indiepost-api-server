package com.indiepost.repository

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
class ProfileRepositoryTests {
    @Inject
    private lateinit var repository: ProfileRepository

    private val insertedIds = ArrayList<Long>()

    @BeforeEach
    fun before() {
        val profile = Profile(slug = "test")
        profile.created = LocalDateTime.now()
        profile.displayName = "Test User"
        profile.description = "It is a profile for tests"
        profile.email = "sysadmin@indiepost.co.kr"
        profile.label = "title"
        profile.fullName = "Jake Lee"
        profile.showEmail = false
        profile.showDescription = true
        profile.showLabel = true
        profile.profileState = Types.ProfileState.ACTIVE
        profile.lastUpdated = LocalDateTime.now()
        profile.profileType = Types.ProfileType.Writer
        val id = repository.save(profile)
        if (id != null) {
            insertedIds.add(id)
        }
    }

    @Test
    fun findById_shouldWorkProperly() {
        val id = insertedIds[insertedIds.size - 1]
        assertThat(id).isNotNull()
        val profile = repository.findById(id)
        assertThat(profile).isNotNull()
        if (profile != null) {
            assertThat(profile.id).isNotNull()
        }
    }

    @Test
    fun findBySlug_shouldWorkProperly() {
        val id = insertedIds[insertedIds.size - 1]
        assertThat(id).isNotNull()
        val profile = repository.findBySlug("test")
        assertThat(profile).isNotNull()
        if (profile != null) {
            assertThat(profile.id).isNotNull()
            assertThat(profile.slug).isEqualTo("test")
        }
    }

    @Test
    fun findAllByProfileType_shouldWorkProperly() {
        val id = insertedIds[insertedIds.size - 1]
        assertThat(id).isNotNull()
        val profileList0 = repository.findAllByProfileType(Types.ProfileType.Editor, PageRequest.of(0, 10))
        val profileList1 = repository.findAllByProfileType(Types.ProfileType.Writer, PageRequest.of(0, 10))
        assertThat(profileList0).isNotNull()
        assertThat(profileList1).isNotNull()
        assertThat(profileList0.content.size).isEqualTo(0)
        assertThat(profileList1.content.size).isEqualTo(1)
    }
}
