package com.indiepost.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by jake on 17. 5. 21.
 */
public class LocalDateTimeSerializerCustom extends LocalDateTimeSerializer {
    public static final LocalDateTimeSerializerCustom INSTANCE = new LocalDateTimeSerializerCustom();
    private static final long serialVersionUID = 1L;

    protected LocalDateTimeSerializerCustom() {
        this(null);
    }

    public LocalDateTimeSerializerCustom(DateTimeFormatter f) {
        super(f);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider)
            throws IOException {

        DateTimeFormatter dtf = _formatter;
        if (dtf == null) {
            dtf = _defaultFormatter();
        }
        g.writeString(value.format(dtf));

    }

    // since 2.7: TODO in 2.8; change to use per-type defaulting
    protected DateTimeFormatter _defaultFormatter() {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }
}
