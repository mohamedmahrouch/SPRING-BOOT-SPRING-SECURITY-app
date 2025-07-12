package com.example.demo.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Data
@Document(collection = "validations")
public class Validation {
    @Id
    private String id= UUID.randomUUID().toString();;
    private Instant creation;
    private Instant expire;
    private Instant activation;
    private String code;

    @DBRef
    private User2 utilisateur;
}