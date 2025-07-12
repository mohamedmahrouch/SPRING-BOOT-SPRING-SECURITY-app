package com.example.demo.repository;

import com.example.demo.model.User2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User2, String> {


    List<User2> findByEmail(String email);
    List<User2> findByName(String name);
}
