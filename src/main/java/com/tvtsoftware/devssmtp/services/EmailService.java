package com.tvtsoftware.devssmtp.services;

import com.tvtsoftware.devssmtp.model.Email;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;

public interface EmailService {

    Page<Email> getAllEmailWithPaging(int page);

    Email getEmailByUID(Long id);
    void deleteEmailByUID(Long id);

    @Transactional
    void deleteAllEmail();
}
