package com.tvtsoftware.devssmtp.controller;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.model.EmailAttachment;
import com.tvtsoftware.devssmtp.services.DevsSmtpUtils;
import com.tvtsoftware.devssmtp.services.EmailService;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@Controller
public class DevSmtpController {

    private final EmailService emailService;

    private final DevsSmtpUtils devsSmtpUtils;

    public DevSmtpController(EmailService emailService, DevsSmtpUtils devsSmtpUtils) {
        this.emailService = emailService;
        this.devsSmtpUtils = devsSmtpUtils;
    }

    @GetMapping(value = "")
    public String home(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, Model model) {
        Page<Email> emailPage = emailService.getAllEmailWithPaging(page);
        model.addAttribute("page", emailPage);
        return "email-list";
    }

    @GetMapping(value = "/email/{id}")
    public String emailDetail(@PathVariable("id") Long id, Model model) {
        Email email = emailService.getEmailByUID(id);
        email.getContents().forEach(emailContent -> emailContent.setData(devsSmtpUtils.render(emailContent)));
        model.addAttribute("mail", email);
        return "email";
    }

    @GetMapping(value = "/email/delete/{id}")
    public String deleteEmail(@PathVariable("id") Long id) {
        emailService.deleteEmailByUID(id);
        return "redirect:/";
    }

    @PostMapping(value = "/email/delete-all")
    public String deleteAllEmail() {
        emailService.deleteAllEmail();
        return "redirect:/";
    }


    @GetMapping(value = "/email/download/{id}")
    @ResponseBody
    public ResponseEntity downloadEmail(@PathVariable("id") Long id) throws IOException {
        Email email = emailService.getEmailByUID(id);
        byte[] bytes = email.getRaw().getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(bytes);
        String filename = StringUtils.trim(email.getSubject()) + ".eml";
        return ResponseEntity.ok()
                .contentLength(bytes.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8))
                .body(resource);
    }

    @GetMapping(value = "/email/{emailId}/attachment/{attachmentId}")
    @ResponseBody
    public ResponseEntity downloadEmail(@PathVariable("emailId") Long emailId, @PathVariable("attachmentId") Long attachmentId) throws MimeTypeException {
        Email email = emailService.getEmailByUID(emailId);
        Optional<EmailAttachment> attachment = email.getAttachments().stream().filter(emailAttachment -> Objects.equals(attachmentId, emailAttachment.getId())).findFirst();
        if (attachment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EmailAttachment emailAttachment = attachment.get();
        byte[] bytes = emailAttachment.getData();
        ByteArrayResource resource = new ByteArrayResource(bytes);
        MimeType mimeType = MimeTypes.getDefaultMimeTypes().forName(emailAttachment.getContentType());
        mimeType.getExtension();

        String filename = StringUtils.trim(emailAttachment.getContentId())+ mimeType.getExtension();
        return ResponseEntity.ok()
                .contentLength(bytes.length)
                .header("Content-type", emailAttachment.getContentType())
                .header("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8))
                .body(resource);
    }
}
