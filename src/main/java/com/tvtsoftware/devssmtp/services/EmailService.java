package com.tvtsoftware.devssmtp.services;

import com.tvtsoftware.devssmtp.model.Email;

import java.util.List;

public interface EmailService {

    List<Email> getAllEmailWithPaging(int page);
}
