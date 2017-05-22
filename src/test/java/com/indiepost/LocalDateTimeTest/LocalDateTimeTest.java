package com.indiepost.LocalDateTimeTest;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 5. 22.
 */
public class LocalDateTimeTest {
    @Test
    public void iso8601zStringDeserializationTest() {
        String iso = "2017-04-30";
        LocalDate localDate = LocalDate.parse(iso);
        LocalDateTime startDateTime = localDate.atStartOfDay();
        LocalDateTime endDateTime = localDate.atTime(23, 59, 59);
        System.out.println(startDateTime);
        System.out.println(endDateTime);
    }
}
