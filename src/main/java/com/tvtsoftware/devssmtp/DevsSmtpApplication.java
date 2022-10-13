package com.tvtsoftware.devssmtp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DevsSmtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevsSmtpApplication.class, args);
    }

}
