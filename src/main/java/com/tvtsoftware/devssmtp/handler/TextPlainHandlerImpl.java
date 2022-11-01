package com.tvtsoftware.devssmtp.handler;

import com.tvtsoftware.devssmtp.ContentType;
import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.model.EmailAttachment;
import com.tvtsoftware.devssmtp.model.EmailContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TextPlainHandlerImpl implements MultipartHandler {
    @Override
    public Email processEmail(MailEnvelope mailEnvelope) {
        try {
            Session defaultSession = Session.getDefaultInstance(new Properties());
            MimeMessage mimeMessage = new MimeMessage(defaultSession, mailEnvelope.getMessageInputStream());
            MimeMessageParser mimeMessageParser = new MimeMessageParser(mimeMessage);
            mimeMessageParser.parse();

            String emailContentRaw = StringUtils.defaultIfEmpty(mimeMessageParser.getHtmlContent(), mimeMessageParser.getPlainContent());
            Email email = new Email();
            email.setSubject(mimeMessageParser.getSubject());
            email.setRaw(IOUtils.toString(mailEnvelope.getMessageInputStream(), StandardCharsets.UTF_8));
            email.setRawBody(emailContentRaw);
            email.setReceivedOn(new Date());
            email.setFromAddress(mimeMessageParser.getFrom());
            email.setToAddress(mimeMessageParser.getTo().get(0).toString());
            if (mimeMessageParser.hasAttachments()) {
                Map<String, DataSource> cidMap = mimeMessageParser.getContentIds().stream()
                        .collect(Collectors.toMap(Function.identity(), mimeMessageParser::findAttachmentByCid));
                for (Map.Entry<String, DataSource> attachment : cidMap.entrySet()) {
                    EmailAttachment emailAttachment = new EmailAttachment();
                    emailAttachment.setData(attachment.getValue().getInputStream().readAllBytes());
                    emailAttachment.setEmail(email);
                    emailAttachment.setFilename(attachment.getValue().getName());
                    emailAttachment.setContenttype(attachment.getValue().getContentType());
                    emailAttachment.setContentId(attachment.getKey());
                    email.addAttachment(emailAttachment);
                }
            }
            EmailContent emailContent = new EmailContent();
            emailContent.setContentType(ContentType.fromValue(mimeMessage.getContentType().split(";")[0]));
            emailContent.setData(emailContentRaw);
            email.addContent(emailContent);
            return email;
        } catch (Exception e) {
            log.warn("Parsing mime-message from receiving email cause error {}", e.getMessage());
            return null;
        }
    }
}
