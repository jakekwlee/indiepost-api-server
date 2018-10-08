package com.indiepost.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadSystemEnvironmentTests {

    @Test
    public void signingSecretShouldReturnProperly() {
        String signingSecret = System.getenv("AUTH0_SECRET");
        assertThat(signingSecret).isNotNull();
    }
}
