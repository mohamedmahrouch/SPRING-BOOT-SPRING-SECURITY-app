package com.example.demo.repository;

import com.example.demo.model.Validation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidationRepository extends MongoRepository<Validation, Integer> {

    Validation findByCode(String code);
}
