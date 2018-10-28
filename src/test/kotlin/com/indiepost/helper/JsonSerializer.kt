package com.indiepost.helper

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module

fun printToJson(o: Any) {
    val objectMapper = ObjectMapper()
    objectMapper.registerModule(Hibernate5Module())
    try {
        val result = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
                .writeValueAsString(o)
        println("Size of results: " + result.toByteArray().size / 1024.0 + " kb")
        println(result)
    } catch (e: JsonProcessingException) {
        e.printStackTrace()
    }

}
