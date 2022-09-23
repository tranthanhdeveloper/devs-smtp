package com.tvtsoftware.devssmtp.config;

import com.tvtsoftware.devssmtp.handler.MultipartHandler;
import com.tvtsoftware.devssmtp.server.SMTPServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.protocols.api.handler.ProtocolHandler;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.MessageHook;
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
    public Map<String, MultipartHandler> multipartHandlerResolverMap(@Qualifier("textPlainHandlerImpl") MultipartHandler plainTextHandler) {
        Map<String, MultipartHandler> handlerMap = new HashMap<>();
        handlerMap.put("text/plain", plainTextHandler);
        return handlerMap;
    }

    @Bean
    public ProtocolHandler loggingMessageHook() {
        return new MessageHook() {
            @Override
            public HookResult onMessage(SMTPSession smtpSession, MailEnvelope mailEnvelope) {
                log.info("mail from={} to={} size={}", mailEnvelope.getSender(), mailEnvelope.getRecipients(), mailEnvelope.getSize());
                return HookResult.OK;
            }

            @Override
            public void init(org.apache.commons.configuration.Configuration configuration) {
            }

            @Override
            public void destroy() {
            }
        };
    }
}
