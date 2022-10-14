package com.tvtsoftware.devssmtp.handler;

public interface MultipartHandlerResolver {
    MultipartHandler resolve(String contentType);
}
