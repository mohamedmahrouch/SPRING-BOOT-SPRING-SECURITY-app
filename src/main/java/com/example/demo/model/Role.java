package com.example.demo.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("roles")
@Data
public class Role {
    @Id
    private String id = UUID.randomUUID().toString();
    private TypeRole libelle;
    @DBRef
    private User2 users;
}
