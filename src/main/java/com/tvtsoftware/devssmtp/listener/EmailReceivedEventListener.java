package com.tvtsoftware.devssmtp.listener;

import com.tvtsoftware.devssmtp.handler.EmailHandler;
import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.repository.EmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailReceivedEventListener{

    private final EmailRepository emailRepository;
    private final EmailHandler emailHandler;

    public EmailReceivedEventListener(EmailRepository emailRepository, EmailHandler emailHandler) {
        this.emailRepository = emailRepository;
        this.emailHandler = emailHandler;
    }

    @Async
    @EventListener(classes = {EmailReceivedEvent.class})
    @Transactional
    public void onApplicationEvent(EmailReceivedEvent event) {
        Email email = emailHandler.processEmail(event.getMailEnvelope());
        emailRepository.save(email);
    }
}
