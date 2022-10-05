package com.tvtsoftware.devssmtp.services;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.repository.EmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private static final int DEFAULT_PAGE_SIZE = 100;
    private final EmailRepository emailRepository;

    public EmailServiceImpl(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public Page<Email> getAllEmailWithPaging(int page) {
        PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        pageRequest.withSort(Sort.by(Sort.Direction.DESC, "id"));
        Page<Email> result = emailRepository.findAll(pageRequest);
        return result;
    }

    @Override
    public Email getEmailByUID(Long id) {
        Optional<Email> email = emailRepository.findById(id);
        if (email.isPresent()){
            return email.get();
        }
        log.info("email not found for id: {}", id);
        return null;
    }
}
