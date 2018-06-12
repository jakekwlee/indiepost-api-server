package com.indiepost.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;

import java.time.*;

import static com.indiepost.utils.DateUtil.localDateTimeToInstant;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by jake on 17. 5. 22.
 */
public class LocalDateTimeUnitTests {
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

    @Test
    public void localDateTime_shouldSerializeToISO8601String() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LocalDateTime time = LocalDateTime.of(2017, 5, 25, 22, 00, 55);
        String result = mapper.writeValueAsString(time);
        String expected = "\"2017-05-25T22:00:55\"";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void instant_shouldSerializeToISO8601String() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LocalDateTime time = LocalDateTime.of(2017, 5, 25, 22, 00, 55);
        Instant instant = localDateTimeToInstant(time);
        String result = mapper.writeValueAsString(instant);
        String expected = "\"2017-05-25T13:00:55Z\"";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void instant_shouldConvertLocalDateTimeProperly() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LocalDateTime time = LocalDateTime.of(2017, 5, 25, 22, 00, 55);
        Instant instant = time.atOffset(ZoneOffset.of("+09:00")).toInstant();
        LocalDateTime ldt = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        String result = mapper.writeValueAsString(ldt);
        String expected = "\"2017-05-25T22:00:55\"";
        assertThat(result).isEqualTo(expected);
    }
}
