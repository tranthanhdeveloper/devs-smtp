package com.tvtsoftware.devssmtp.services;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.model.EmailAttachment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmaiAttachmentlServiceImpl implements EmaiAttachmentlService {

    @Override
    public Email processInlineImage(Email email) {
        Map<String, byte[]> attachmentMap = email.getAttachments().stream().collect(Collectors.toMap(EmailAttachment::getContentId, EmailAttachment::getData));
        return null;
    }
}
