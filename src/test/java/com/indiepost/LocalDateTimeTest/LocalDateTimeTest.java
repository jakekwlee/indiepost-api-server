package com.indiepost.LocalDateTimeTest;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Created by jake on 17. 5. 22.
 */
public class LocalDateTimeTest {
    @Test
    public void iso8601zStringDeserializationTest() {
        String iso = "2017-04-30T15:00:00.000Z";
        LocalDateTime localDateTime = ZonedDateTime.parse(iso).withZoneSameLocal(ZonedDateTime.now().getOffset()).toLocalDateTime();
        System.out.println(localDateTime);
    }
}
