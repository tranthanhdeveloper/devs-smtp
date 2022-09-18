package com.tvtsoftware.devssmtp.server;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.repository.EmailRepository;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.apache.james.protocols.smtp.hook.MessageHook;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Component
public class PersistenceProtocolHandler implements MessageHook {
    private EmailRepository emailRepository;

    @Override
    public HookResult onMessage(SMTPSession smtpSession, MailEnvelope mailEnvelope) {
        Email email = new Email();
        email.setFromAddress(mailEnvelope.getSender().toString());
        try {
            Session defaultSession = Session.getDefaultInstance(new Properties());
            MimeMessage mimeMessage = new MimeMessage(defaultSession, mailEnvelope.getMessageInputStream());
            email.setSubject(mimeMessage.getSubject());
            email.setRawData(IOUtils.toString(mimeMessage.getRawInputStream(), StandardCharsets.UTF_8));
            email.setReceivedOn(mimeMessage.getReceivedDate());

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
