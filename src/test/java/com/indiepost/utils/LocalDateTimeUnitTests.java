package com.indiepost.utils;

import org.junit.Test;

import java.time.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by jake on 17. 5. 22.
 */
public class LocalDateTimeUnitTests {
    @Test
    public void iso8601DateStringDeserializationTest() {
        String iso8601 = "2017-05-25T22:00:55.000Z";
        Instant source = Instant.parse(iso8601);
        LocalDateTime result = source.atZone(ZoneId.systemDefault()).toLocalDateTime();

        assertEquals("2017-05-26T07:00:55", result.toString());

        LocalDateTime localDateTime = LocalDateTime.now();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.of("+18:00"));
        assertEquals(localDateTime.toString() + "+18:00", offsetDateTime.toString());
    }
}
