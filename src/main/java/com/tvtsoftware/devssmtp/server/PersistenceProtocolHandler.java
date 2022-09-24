package com.tvtsoftware.devssmtp.server;

import com.tvtsoftware.devssmtp.handler.MultipartHandler;
import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.repository.EmailRepository;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.MimeMessageUtils;
import org.apache.james.mime4j.util.MimeUtil;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.MessageHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Component
public class PersistenceProtocolHandler implements MessageHook {
    private final EmailRepository emailRepository;

    private final MultipartHandlerResolver multipartHandlerResolver;

    public PersistenceProtocolHandler(EmailRepository emailRepository, MultipartHandlerResolver multipartHandlerResolver) {
        this.emailRepository = emailRepository;
        this.multipartHandlerResolver = multipartHandlerResolver;
    }

    @Override
    public HookResult onMessage(SMTPSession smtpSession, MailEnvelope mailEnvelope) {
        try {
            Session defaultSession = Session.getDefaultInstance(new Properties());
            MimeMessage mimeMessage = new MimeMessage(defaultSession, mailEnvelope.getMessageInputStream());
            String extractedMimeContentType = StringUtils.defaultString(mimeMessage.getContentType().split(";")[0], "");
            MultipartHandler handler = multipartHandlerResolver.resolve(extractedMimeContentType);
            if (Objects.nonNull(handler)) {
                Email email = handler.processEmail(mimeMessage);
                emailRepository.save(email);
            }

        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void init(Configuration configuration) throws ConfigurationException {

    }

    @Override
    public void destroy() {

    }
}
