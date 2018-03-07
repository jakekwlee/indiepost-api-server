package com.indiepost.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by jake on 17. 5. 21.
 */
public class LocalDateTimeToUtcStringSerializer extends LocalDateTimeSerializer {
    public static final LocalDateTimeToUtcStringSerializer INSTANCE = new LocalDateTimeToUtcStringSerializer();
    private static final long serialVersionUID = 1L;

    protected LocalDateTimeToUtcStringSerializer() {
        this(null);
    }

    public LocalDateTimeToUtcStringSerializer(DateTimeFormatter f) {
        super(f);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider)
            throws IOException {
        g.writeString(value.atZone(ZoneId.of("Asia/Seoul")).toInstant().toString());
    }
}
