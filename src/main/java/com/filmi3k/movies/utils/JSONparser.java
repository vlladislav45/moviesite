package com.filmi3k.movies.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import java.io.IOException;

public class JSONparser {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(JSONparser.class);

    public static String toJson(Object object)
    {
        try
        {
            log.info("succ");
            return objectMapper.writer().writeValueAsString(object);
        } catch (JsonProcessingException e)
        {
            log.error("Jackson writer didn't work properly.", e);
        }
        return "";
    }

    public static String toJsonArr(List<?> objects) {
        ArrayNode array = objectMapper.createArrayNode();

        for (Object obj : objects) {
            try {
                array.add(objectMapper.createArrayNode().add(objectMapper.writeValueAsString(obj)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return array.toPrettyString();
    }

    public static <T> T toObject(String json, Class<T> objectClass)
    {
        try
        {
            return objectMapper.readValue(json, objectClass);
        } catch (IOException e)
        {
            log.error("Not a valid Json.", e);
            return null;
        }
    }
}
