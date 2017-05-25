package com.indiepost.dateTimeTest;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * Created by jake on 17. 5. 22.
 */
public class LocalDateTimeTest {
    @Test
    public void iso8601zStringDeserializationTest() {
        String iso8601 = "2007-12-03T10:15:30+08:00";
        OffsetDateTime source = OffsetDateTime.parse(iso8601);
        LocalDateTime result = source.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

        System.out.println(source);
        System.out.println(result);
    }
}
