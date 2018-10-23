package com.indiepost.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReadSystemEnvironmentTests {

    @Test
    fun signingSecretShouldReturnProperly() {
        val signingSecret = System.getenv("AUTH0_SECRET")
        assertThat(signingSecret).isNotNull()
    }
}
