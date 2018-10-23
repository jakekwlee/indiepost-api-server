package com.indiepost.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Created by jake on 17. 5. 22.
 */
class LocalDateTimeUnitTests {
    @Test
    fun iso8601zStringDeserializationTest() {
        val iso8601 = "2017-05-25T22:00:55.000Z"
        val source = Instant.parse(iso8601)
        val result = source.atZone(ZoneId.systemDefault()).toLocalDateTime()

        println(source)
        println(result)

        val localDateTime = LocalDateTime.now()
        val offsetDateTime = localDateTime.atOffset(ZoneOffset.of("+18:00"))
        println(localDateTime)
        println(offsetDateTime)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun localDateTime_shouldSerializeToISO8601String() {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val time = LocalDateTime.of(2017, 5, 25, 22, 0, 55)
        val result = mapper.writeValueAsString(time)
        val expected = "\"2017-05-25T22:00:55\""
        assertThat(result).isEqualTo(expected)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun instant_shouldSerializeToISO8601String() {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val time = LocalDateTime.of(2017, 5, 25, 22, 0, 55)
        val instant = localDateTimeToInstant(time)
        val result = mapper.writeValueAsString(instant)
        val expected = "\"2017-05-25T13:00:55Z\""
        assertThat(result).isEqualTo(expected)
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun instant_shouldConvertLocalDateTimeProperly() {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val time = LocalDateTime.of(2017, 5, 25, 22, 0, 55)
        val instant = time.atOffset(ZoneOffset.of("+09:00")).toInstant()
        val ldt = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        val result = mapper.writeValueAsString(ldt)
        val expected = "\"2017-05-25T22:00:55\""
        assertThat(result).isEqualTo(expected)
    }
}
