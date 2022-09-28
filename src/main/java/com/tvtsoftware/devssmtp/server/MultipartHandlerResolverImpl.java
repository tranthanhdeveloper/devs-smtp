package com.tvtsoftware.devssmtp.server;

import com.tvtsoftware.devssmtp.handler.MultipartHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MultipartHandlerResolverImpl implements MultipartHandlerResolver {
    @Autowired
    @Qualifier(value = "multipartHandlerResolverMap")
    private Map<String, MultipartHandler> handler;

    @Override
    public MultipartHandler resolve(String contentType) {
        log.info("Resolving handler");
        if (!handler.containsKey(contentType)) {
            log.info("No handler found for content type {}", contentType);
            return null;
        }
        return handler.get(contentType);
    }
}
