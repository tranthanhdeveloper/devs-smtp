package com.tvtsoftware.devssmtp;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DevSmtpController {

    @Autowired
    private EmailService emailService;

    @ResponseBody
    @GetMapping(value = "")
    public List<Email> home() {
        return emailService.getAllEmailWithPaging(0);
    }
}
