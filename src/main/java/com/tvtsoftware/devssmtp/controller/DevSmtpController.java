package com.tvtsoftware.devssmtp.controller;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.services.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DevSmtpController {

    private final EmailService emailService;

    public DevSmtpController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping(value = "")
    public String home(Model model) {
        Page<Email> emailPage = emailService.getAllEmailWithPaging(0);
        model.addAttribute("page", emailPage);
        return "email-list";
    }

    @GetMapping(value = "/email/{id}")
    public String emailDetail(@PathVariable("id") Long id,  Model model) {
        Email email = emailService.getEmailByUID(id);
        model.addAttribute("mail", email);
        return "email";
    }
}
