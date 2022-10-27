package com.tvtsoftware.devssmtp.services;

import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.repository.EmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Value(value = "${smtp.server.page-size}")
    private int defaultPageSize;
    private final EmailRepository emailRepository;

    public EmailServiceImpl(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public Page<Email> getAllEmailWithPaging(int page) {
        PageRequest pageRequest = PageRequest.of(page, defaultPageSize)
                .withSort(Sort.by(Sort.Direction.DESC, "receivedOn"));
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

    @Override
    @Transactional
    public void deleteEmailByUID(Long id) {
        emailRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllEmail() {
        emailRepository.deleteAll();
    }
}
