package com.indiepost.repository

import com.indiepost.IndiepostBackendApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [IndiepostBackendApplication::class])
@WebAppConfiguration
@Transactional
class TagRepositoryTests {
    @Inject
    private lateinit var tagRepository: TagRepository

    @Test
    fun findByNameIn_shouldReturnTagListProperly() {
        val tagNames = Arrays.asList("TV드라마", "2012clothing", "24City")
        val tags = tagRepository.findByNameIn(tagNames)
        val expected = arrayOfNulls<String>(3)
        val result = arrayOfNulls<String>(3)
        tagNames.stream().map { t -> t.toLowerCase() }.collect(Collectors.toList()).toTypedArray()
        tags.stream().map { (_, name) -> name!!.toLowerCase() }.collect(Collectors.toList()).toTypedArray()
        assertThat<String>(result).isEqualTo(expected)
    }

    @Test
    fun updateSelected_shouldExecuteWithoutError() {
        val tagNames = Arrays.asList("Music", "Film")
        tagRepository.updateSelected(tagNames)
        val tags = tagRepository.findSelected()
        val result = tags.map { it.name }
        assertThat(result).isEqualTo(tagNames)
    }
}
