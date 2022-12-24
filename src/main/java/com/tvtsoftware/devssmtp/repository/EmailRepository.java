package com.tvtsoftware.devssmtp.repository;

import com.tvtsoftware.devssmtp.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

}
