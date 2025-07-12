package com.example.demo.service;

import com.example.demo.model.Validation;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Data
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
        @Autowired
        JavaMailSender javaMailSender;


        public void envoyer(Validation validation) {
            log.info("envoyer Email started");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mahrouchmohamed94@gmail.com");
            message.setTo(validation.getUtilisateur().getEmail());
            message.setSubject("Votre code d'activation");

            String texte = String.format(
                    "Bonjour %s, Votre code d'action est %s  ; A bientôt",
                    validation.getUtilisateur().getName(),
                    validation.getCode()
            );
            message.setText(texte);

            javaMailSender.send(message);
            log.info("envoyer email finished");
        }

}