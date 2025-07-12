package com.example.demo.service;

import com.example.demo.model.User2;
import com.example.demo.model.Validation;
import com.example.demo.repository.ValidationRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;


@Service
@Data
public class ValidationService {

    private static final Logger log = LoggerFactory.getLogger(ValidationService.class);
    private ValidationRepository validationRepository;
    @Autowired
    private  NotificationService notificationService;

    public  ValidationService(ValidationRepository validationRepository) {
        this.validationRepository = validationRepository;

    }
    public Validation createValidation(User2 user) {
        log.info("Creating validation dans Service validation for user {}", user);

        Validation validation = new Validation();
        validation.setUtilisateur(user);

        Instant creation = Instant.now();
        validation.setCreation(creation);
        Instant expiration = Instant.now().plus(2, MINUTES);
        validation.setExpire(expiration);

        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);
        validation.setCode(code);
        log.info("validation finished");
        log.info("start to send email");
        this.notificationService.envoyer(validation);
        log.info("email sent");
        log.info("validation created with code: {}", validation.getCode());
        return validationRepository.save(validation);

    }
    public Validation  verification(String code) {
        log.info("Verification code: {}", code);
        Validation validation = validationRepository.findByCode(code);
        if (validation == null) {
            log.error("Validation not found for code: {}", code);
            throw new RuntimeException("Validation not found");
        }
        Instant now = Instant.now();
        if (validation.getExpire().isBefore(now)) {
            log.error("Validation expired for code: {}", code);
            throw new RuntimeException("Validation expired");
        }
        log.info("Validation successful for code: {}", code);
        return validation;
    }
}
