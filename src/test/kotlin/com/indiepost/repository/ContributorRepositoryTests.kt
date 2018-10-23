package com.indiepost.repository

import com.indiepost.NewIndiepostApplication
import com.indiepost.enums.Types
import com.indiepost.model.Contributor
import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [NewIndiepostApplication::class])
@WebAppConfiguration
@Transactional
class ContributorRepositoryTests {

    @Inject
    private lateinit var contributorRepository: ContributorRepository

    private val insertedId = ArrayList<Long>()

    @BeforeEach
    fun beforeTests() {
        val contributor = Contributor()
        contributor.fullName = RandomStringUtils.randomAlphanumeric(10)
        contributor.email = "before@example.com"
        contributor.subEmail = "test_before_sub@example.com"
        contributor.description = "Hello World!"
        contributor.displayType = Types.ContributorDisplayType.TEXT
        contributor.title = "Test Before title"
        contributor.contributorType = Types.ContributorType.FreelanceEditor

        val now = LocalDateTime.now()
        contributor.lastUpdated = now
        contributor.created = now

        val id = contributorRepository.save(contributor)
        insertedId.add(id)
    }

    @Test
    fun save_shouldSaveEntityAndReturnIdProperly() {
        val contributor = Contributor()
        contributor.fullName = RandomStringUtils.randomAlphanumeric(10)
        contributor.email = "test@example.com"
        contributor.subEmail = "test_sub@example.com"
        contributor.description = "Hello World"
        contributor.displayType = Types.ContributorDisplayType.TEXT
        contributor.title = "Test Title"
        contributor.contributorType = Types.ContributorType.FeatureEditor

        val now = LocalDateTime.now()
        contributor.lastUpdated = now
        contributor.created = now

        val id = contributorRepository.save(contributor)
        if (id != null) {
            insertedId.add(id)
        }
        assertThat(id).isNotNull()
    }

    @Test
    fun delete_shouldDeleteContributorProperly() {
        var contributors = contributorRepository.findAll(
                PageRequest.of(0, 10, Sort.Direction.DESC, "id")
        ).content
        if (contributors.size > 0) {
            for (contributor in contributors) {
                contributorRepository.delete(contributor)
            }
        }
        contributors = contributorRepository.findAll(
                PageRequest.of(0, 10, Sort.Direction.DESC, "id")).content
        assertThat(contributors.size).isEqualTo(0)
    }

    @AfterEach
    fun afterTests() {
        for (id in insertedId) {
            contributorRepository.deleteById(id)
        }
    }
}
