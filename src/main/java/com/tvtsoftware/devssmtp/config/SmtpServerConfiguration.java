package com.tvtsoftware.devssmtp.config;

import com.tvtsoftware.devssmtp.handler.EmailHandler;
import com.tvtsoftware.devssmtp.server.SMTPServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.protocols.api.handler.ProtocolHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Configuration
@Slf4j
@EnableConfigurationProperties(SmtpServerProperties.class)
public class SmtpServerConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public SMTPServer smtpServer(SmtpServerProperties properties, Collection<ProtocolHandler> handlers) {
        return new SMTPServer(properties, handlers);
    }

    @Bean
    public Map<String, EmailHandler> multipartHandlerResolverMap(@Qualifier("universalHandlerImpl") EmailHandler plainTextHandler) {
        Map<String, EmailHandler> handlerMap = new HashMap<>();
        handlerMap.put("text/plain", plainTextHandler);
        handlerMap.put("multipart/mixed", plainTextHandler);
        return handlerMap;
    }
}
