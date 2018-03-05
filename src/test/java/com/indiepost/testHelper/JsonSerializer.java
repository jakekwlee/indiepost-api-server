package com.indiepost.testHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

public interface JsonSerializer {

    static void printToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        try {
            String result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                    .writeValueAsString(object);
            System.out.println("Size of results: " + (result.getBytes().length / 1024.0) + " kb");
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
