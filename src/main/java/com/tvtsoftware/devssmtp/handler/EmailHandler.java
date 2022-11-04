package com.tvtsoftware.devssmtp.handler;

import com.tvtsoftware.devssmtp.model.Email;
import org.apache.james.protocols.smtp.MailEnvelope;

public interface EmailHandler {
    Email processEmail(MailEnvelope mailEnvelope);
}
