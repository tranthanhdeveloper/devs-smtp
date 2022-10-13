package com.tvtsoftware.devssmtp.handler;

import com.tvtsoftware.devssmtp.handler.MultipartHandler;

public interface MultipartHandlerResolver {
    MultipartHandler resolve(String contentType);
}
