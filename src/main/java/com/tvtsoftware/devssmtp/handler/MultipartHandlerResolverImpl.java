package com.tvtsoftware.devssmtp.handler;

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

    @Autowired
    @Qualifier(value = "textPlainHandlerImpl")
    private MultipartHandler multipartHandler;

    @Override
    public MultipartHandler resolve(String contentType) {
        log.info("Resolving handler");
        if (!handler.containsKey(contentType)) {
            log.info("No handler found for content type {}", contentType);
            return multipartHandler;
        }
        return handler.get(contentType);
    }


}
