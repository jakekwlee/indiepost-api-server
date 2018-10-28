package com.indiepost.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RegexUnitTests {
    @Test
    fun orRegexShouldWorksProperly() {
        val cases = arrayOf("Spider mine", "mine spider", "yeti browser", "python request", "PythonR")
        for (c in cases) {
            val result = c.contains(Regex("spider|python|yeti", RegexOption.IGNORE_CASE))
            assertThat(result).isTrue()
        }
    }
}
