package com.tvtsoftware.devssmtp.controller;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.services.EmailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class DevSmtpController {

    private final EmailService emailService;

    public DevSmtpController(EmailService emailService) {
        this.emailService = emailService;
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
}
