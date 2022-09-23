package com.tvtsoftware.devssmtp.handler;

import com.tvtsoftware.devssmtp.model.Email;

import javax.mail.internet.MimeMessage;

public interface MultipartHandler {
    Email processEmail(MimeMessage mimeMessage);
}
