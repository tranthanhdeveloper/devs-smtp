package com.tvtsoftware.devssmtp.server;

import com.tvtsoftware.devssmtp.listener.EmailReceivedEvent;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.MessageHook;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PersistenceProtocolHandler implements MessageHook {
    private ApplicationEventPublisher applicationEventPublisher;

    public PersistenceProtocolHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void init(Configuration configuration) throws ConfigurationException {

    }

    @Override
    public HookResult onMessage(SMTPSession session, MailEnvelope mail) {
        applicationEventPublisher.publishEvent(new EmailReceivedEvent(session, mail));
        return HookResult.OK;
    }

    @Override
    public void destroy() {

    }


}
