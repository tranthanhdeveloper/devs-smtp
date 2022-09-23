package com.tvtsoftware.devssmtp.services;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    private static final int DEFAULT_PAGE_SIZE = 100;
    @Autowired
    private EmailRepository emailRepository;
    @Override
    public List<Email> getAllEmailWithPaging(int page) {
        PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<Email> result = emailRepository.findAll(pageRequest);
        return result.toList();
    }
}
