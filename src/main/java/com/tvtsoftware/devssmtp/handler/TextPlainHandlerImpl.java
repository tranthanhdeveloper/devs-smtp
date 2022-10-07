package com.tvtsoftware.devssmtp.handler;

import com.tvtsoftware.devssmtp.ContentType;
import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.model.EmailAttachment;
import com.tvtsoftware.devssmtp.model.EmailContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class TextPlainHandlerImpl implements MultipartHandler {
    @Override
    public Email processEmail(MimeMessage mimeMessage) {
        try {
            Email email = new Email();
            MimeMessageParser mimeMessageParser = new MimeMessageParser(mimeMessage);
            mimeMessageParser.parse();
            email.setSubject(mimeMessageParser.getSubject());
            email.setRaw(IOUtils.toString(mimeMessage.getRawInputStream(), StandardCharsets.UTF_8));
            email.setRawBody(mimeMessageParser.getHtmlContent());
            email.setReceivedOn(new Date());
            email.setFromAddress(mimeMessageParser.getFrom());
            email.setToAddress(mimeMessageParser.getTo().get(0).toString());
            if (mimeMessageParser.hasAttachments()) {
                List<DataSource> attachmentList = mimeMessageParser.getAttachmentList();
                for (DataSource dataSource : attachmentList) {
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setEmail(email);
                    attachment.setData(dataSource.getInputStream().readAllBytes());
                    attachment.setFilename(dataSource.getName());
                    attachment.setContenttype(dataSource.getContentType());
                    email.addAttachment(attachment);
                }
            }
            EmailContent emailContent = new EmailContent();
            emailContent.setContentType(ContentType.fromValue(mimeMessage.getContentType().split(";")[0]));
            emailContent.setData(mimeMessageParser.getHtmlContent());
            email.addContent(emailContent);
            return email;
        } catch (Exception e) {
            log.warn("Parsing mime-message from receiving email cause error {}", e.getMessage());
            return null;
        }
    }
}
