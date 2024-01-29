package com.wts.tsrpc.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wts.tsrpc.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class JacksonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);

    private static final ObjectMapper mapper = (new ObjectMapper()).registerModule(new JavaTimeModule());

    public static String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BizException("Serialize object error !");
        }
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BizException("Deserialize string error !");
        }
    }

    public static <B> B parseObject(String json, Type type) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.constructType(type);
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        ObjectReader objectReader = objectMapper.reader().forType(javaType);
        try {
            return objectReader.readValue(inputStream);
        } catch (IOException e) {
            logger.error("Convert json to object error: ", e);
            throw new BizException(STR."Convert json to object error: \{e.getMessage()}");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("Close input stream error: ", e);
            }
        }
    }
}
