package com.quest.badminton.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtil {

    public static String toJson(Object object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Hỗ trợ Java Time (Instant, LocalDateTime, ...)
        mapper.registerModule(new JavaTimeModule());

        // Instant -> ISO-8601 thay vì timestamp
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // BigDecimal: không dùng scientific notation
        mapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);

        mapper.enable(JsonGenerator.Feature.COMBINE_UNICODE_SURROGATES_IN_UTF8);

        // Optional nhưng nên có
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper;
    }
}
