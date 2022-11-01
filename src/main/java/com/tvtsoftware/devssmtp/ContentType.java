package com.tvtsoftware.devssmtp;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ContentType {
    PLAIN("text/plain"),
    HTML("text/html"),
    TEXT_XML("text/xml"),
    MULTIPART_ALTERNATIVE("multipart/alternative"),
    MULTIPART_MIXED("multipart/mixed"),
    MULTIPART_RELATED("multipart/related"),
    IMAGE("image/"),
    UNDEFINED("");
    private String value;

    private static final Map<String, ContentType> MAP = Stream.of(ContentType.values()).collect(Collectors.toMap(ContentType::getValue, Function.identity()));

    public static ContentType fromValue(String value) {
        return MAP.get(value);
    }

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
