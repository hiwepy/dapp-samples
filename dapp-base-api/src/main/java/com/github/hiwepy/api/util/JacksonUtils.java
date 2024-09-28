package com.github.hiwepy.api.util;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class JacksonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
    }

    private JacksonUtils() {
    }

    public static <T> String obj2json(T entity) {
        String json = null;

        try {
            json = mapper.writeValueAsString(entity);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
        }

        return json;
    }

    public static <T> String obj2str(T entity) {
        return obj2json(entity);
    }

    public static <T> byte[] obj2jsonBytes(T entity) throws Exception {
        return mapper.writeValueAsBytes(entity);
    }

    public static <T> JsonNode obj2node(T entity) throws Exception {
        return mapper.valueToTree(entity);
    }

    public static <T> boolean write2jsonFile(String filepath, T entity) throws Exception {
        File file = new File(filepath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var4) {
                var4.printStackTrace();
                return false;
            }
        }

        return write2jsonFile(new File(filepath), entity);
    }

    public static <T> boolean write2jsonFile(File file, T entity) throws Exception {
        try {
            mapper.writeValue(file, entity);
            return true;
        } catch (FileNotFoundException var3) {
            log.error("File not exists");
            var3.printStackTrace();
            return false;
        }
    }

    public static <T> T json2obj(String json, Class<T> type) throws Exception {
        return mapper.readValue(json, type);
    }

    public static <T> T str2obj(String json, Class<T> type) throws Exception {
        return json2obj(json, type);
    }

    public static Map<String, Object> json2map(String json) throws Exception {
        return (Map) mapper.readValue(json, Map.class);
    }

    public static <T> Map<String, T> json2map(String json, Class<T> type) throws Exception {
        return (Map) mapper.readValue(json, new TypeReference<Map<String, T>>() {
        });
    }

    public static <T> T map2obj(Map map, Class<T> type) throws Exception {
        return mapper.convertValue(map, type);
    }

    public static <T> T parseJSON(File json, Class<T> type) throws Exception {
        return mapper.readValue(json, type);
    }

    public static <T> T parseJSON(URL url, Class<T> type) throws Exception {
        return mapper.readValue(url, type);
    }

    public static <T> List<T> json2list(String json, Class<T> T) {
        CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, T);
        try {
            return (List) mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> str2list(String json, Class<T> T) throws Exception {
        return json2list(json, T);
    }

    public static JsonNode json2node(String json) throws JsonProcessingException {
        return mapper.readTree(json);
    }

    public static JsonNode str2node(String json) throws Exception {
        return json2node(json);
    }

    public static ObjectNode objectNode() {
        return JsonNodeFactory.instance.objectNode();
    }

    public static boolean isJsonString(String json) {
        try {
            mapper.readTree(json);
            return true;
        } catch (Exception var2) {
            if (log.isDebugEnabled()) {
                log.info("check input string is json format;json : " + json + " ; exception;"
                        + var2.getMessage());
            }

            return false;
        }
    }

    public static JsonNode[] node2Array(JsonNode result) {
        if (Objects.isNull(result)) {
            return new JsonNode[0];
        }
        JsonNode[] nodes = new JsonNode[result.size()];
        for (int i = 0; i < result.size(); i++) {
            nodes[i] = result.get(i);
        }
        return nodes;
    }

    public static ArrayNode arrayNode() {
        return JsonNodeFactory.instance.arrayNode();
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap();
        map.put("role", "1001");
        map.put("time", "2016-07-07");
        Map<String, String> rtnMap = json2map(obj2json(map), String.class);
        System.out.println(rtnMap);
    }

}
