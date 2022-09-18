package com.tvtsoftware.devssmtp.model;

public enum ContentType {
    HTML("multipart/alternative"),
    PLAIN("text/plain"),
    MULTIPART_ALTERNATIVE("multipart/alternative"),
    MULTIPART_MIXED("multipart/mixed"),
    MULTIPART_RELATED("multipart/related"),
    IMAGE("image/"),
    UNDEFINED("");
    private String value;

    ContentType(String value) {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
