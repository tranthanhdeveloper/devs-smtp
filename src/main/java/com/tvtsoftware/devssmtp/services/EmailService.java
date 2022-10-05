package com.tvtsoftware.devssmtp.services;

import com.tvtsoftware.devssmtp.model.Email;
import org.springframework.data.domain.Page;

public interface EmailService {

    Page<Email> getAllEmailWithPaging(int page);

    Email getEmailByUID(Long id);
}
