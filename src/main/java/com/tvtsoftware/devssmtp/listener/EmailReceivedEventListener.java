package com.tvtsoftware.devssmtp.listener;

import com.tvtsoftware.devssmtp.handler.MultipartHandler;
import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.repository.EmailRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class EmailReceivedEventListener{

    private EmailRepository emailRepository;
    private MultipartHandler multipartHandler;

    public EmailReceivedEventListener(EmailRepository emailRepository, MultipartHandler multipartHandler) {
        this.emailRepository = emailRepository;
        this.multipartHandler = multipartHandler;
    }

    @Async
    @EventListener(classes = {EmailReceivedEvent.class})
    @Transactional
    public void onApplicationEvent(EmailReceivedEvent event) {
        Email email = multipartHandler.processEmail(event.getMailEnvelope());
        emailRepository.save(email);
    }

    @EventListener(classes = {EmailReceivedEvent.class})
    @Transactional
    public void onApplicationEvent1(EmailReceivedEvent event) {
        System.out.println("haha");
    }
}
