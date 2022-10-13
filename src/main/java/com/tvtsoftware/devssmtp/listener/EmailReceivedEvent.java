package com.tvtsoftware.devssmtp.listener;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.james.protocols.smtp.MailEnvelope;
import org.apache.james.protocols.smtp.SMTPSession;

@Data
@AllArgsConstructor
public class EmailReceivedEvent{
    private SMTPSession smtpSession;
    private MailEnvelope mailEnvelope;
}
