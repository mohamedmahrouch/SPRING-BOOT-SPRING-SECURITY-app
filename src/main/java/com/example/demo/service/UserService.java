package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.TypeRole;
import com.example.demo.model.User2;
import com.example.demo.model.Validation;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private ValidationService validationService;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;


    }


    public List<User2> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User2> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User2 createUser(User2 user) {

        log.info("dans le service user :");

        List<User2> list = this.userRepository.findByEmail(user.getEmail());
        if (!list.isEmpty()) {
            throw new RuntimeException("Email already exists");
        }
        if (!user.getEmail().contains("@")|| !user.getEmail().contains(".com")|| !user.getEmail().contains("@gmail.com")) {
            throw new RuntimeException("Email is not valid");
        }
        String motdepasse = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(motdepasse);

        Role role =  new Role();
        role.setLibelle(TypeRole.USER);
        log.info("role: {}", role);
        user.setRole(role);
        log.info("user avant la validation: {}", user);
        this.validationService.createValidation(user);
        log.info("user avant userRepository.save(user): {}", user);
        return userRepository.save(user);
    }

    public User2 updateUser(String id, User2 userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        }).orElse(null);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public List<User2> findusers(String email) {
        return userRepository.findByEmail(email);
    }
    public List<User2> recupereuser(String name) {
        return userRepository.findByName(name);
    }

    public void verification(Map<String, String> activation) {

        Validation validation=this.validationService.verification(activation.get("code"));
        if(Instant.now().isAfter(validation.getExpire())){
            throw new RuntimeException("votre code d'activation est expiré");
        }
        User2 userrecuprer = validation.getUtilisateur();
        log.info("userrecuprer: {}", userrecuprer);
        String iduser =userrecuprer.getId();
        this.userRepository.findById(iduser).ifPresent(user -> {
            user.setActif(true);
            this.userRepository.save(user);
        });
    }
}
