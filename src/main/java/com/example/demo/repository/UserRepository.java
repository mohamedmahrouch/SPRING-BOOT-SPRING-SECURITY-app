package com.example.demo.repository;

import com.example.demo.model.User2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User2, String> {
    Optional<User2> findByEmail(String email);
    Optional<User2> findByName(String name);
}
