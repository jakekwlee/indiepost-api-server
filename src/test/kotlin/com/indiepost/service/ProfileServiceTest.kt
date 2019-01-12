package com.indiepost.service

import com.indiepost.IndiepostBackendApplication
import com.indiepost.enums.Types
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
@Transactional
internal class ProfileServiceTest {
    @Inject
    private lateinit var service: ProfileService

    @Test
    fun findOne_shouldFindEntityProperly() {
        val id = 1L
        assertThat(id).isNotNull()
        val profile = service.findOne(id)
        assertThat(profile).isNotNull()
    }

    @Test
    fun findOneBySlug_shouldWorkProperly() {
        val profile = service.findOneBySlug("samin")
        assertThat(profile).isNotNull()
    }

    @Test
    fun delete_shouldWorkProperly() {
        val profile = service.findOneBySlug("samin")
        assertThat(profile).isNotNull()
        assertThat(profile!!.id).isNotNull()
        profile.id?.let {
            val id = service.deleteById(it)
            assertThat(id!!).isNotNull()
        }
    }

    @Test
    fun count_shouldWorkProperly() {
        val n = service.count(Types.ProfileType.Editor)
        assertThat(n).isGreaterThanOrEqualTo(5)
    }

    @Test
    fun find() {
        val profiles = service.find(Types.ProfileType.Editor, PageRequest.of(0, 10))
        assertThat(profiles).isNotNull()
        assertThat(profiles).isNotEmpty()
        assertThat(profiles.totalElements).isGreaterThanOrEqualTo(5)
        assertThat(profiles.content.size).isGreaterThanOrEqualTo(5)
    }
}