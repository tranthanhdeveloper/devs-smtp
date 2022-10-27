package com.tvtsoftware.devssmtp.controller;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.services.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
