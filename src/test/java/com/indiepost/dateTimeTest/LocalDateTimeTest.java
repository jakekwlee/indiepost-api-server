package com.indiepost.dateTimeTest;

import org.junit.Test;

import java.time.*;

/**
 * Created by jake on 17. 5. 22.
 */
public class LocalDateTimeTest {
    @Test
    public void iso8601zStringDeserializationTest() {
        String iso8601 = "2017-05-25T22:00:55.000Z";
        Instant source = Instant.parse(iso8601);
        LocalDateTime result = source.atZone(ZoneId.systemDefault()).toLocalDateTime();

        System.out.println(source);
        System.out.println(result);

        LocalDateTime localDateTime = LocalDateTime.now();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.of("+18:00"));
        System.out.println(localDateTime);
        System.out.println(offsetDateTime);
    }
}
