package com.tvtsoftware.devssmtp.server;

import com.tvtsoftware.devssmtp.handler.MultipartHandler;

public interface MultipartHandlerResolver {
    MultipartHandler resolve(String contentType);
}
