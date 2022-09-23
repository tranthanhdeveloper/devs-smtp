package com.tvtsoftware.devssmtp.handler;

import com.tvtsoftware.devssmtp.model.Email;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class TextPlainHandlerImpl implements MultipartHandler {
    @Override
    public Email processEmail(MimeMessage mimeMessage) {
        try {
            Email email = new Email();
            email.setSubject(mimeMessage.getSubject());
            email.setRawData(IOUtils.toString(mimeMessage.getRawInputStream(), StandardCharsets.UTF_8));
            email.setReceivedOn(mimeMessage.getReceivedDate());
            email.setFromAddress(mimeMessage.getFrom()[0].toString());
            return email;
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
