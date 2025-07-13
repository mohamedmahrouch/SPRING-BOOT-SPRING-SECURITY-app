package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.TypeRole;
import com.example.demo.model.User2;
import com.example.demo.model.Validation;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       ValidationService validationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;
    }

    @Override
    public User2 loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    public List<User2> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User2> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User2 createUser(User2 user) {
        log.info("dans le service user :");

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (!user.getEmail().matches("^[\\w-\\.]+@gmail\\.com$")) {
            throw new RuntimeException("Email is not valid");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = new Role();
        role.setLibelle(TypeRole.USER);
        user.setRole(role);

        log.info("user avant la validation: {}", user);
        this.validationService.createValidation(user);
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

    public Optional<User2> finduser(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User2> recupereuser(String name) {
        return userRepository.findByName(name);
    }

    public void activer(Map<String, String> activation) {
        Validation validation = this.validationService.verification(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpire())) {
            throw new RuntimeException("votre code d'activation est expiré");
        }
        User2 userrecuprer = validation.getUtilisateur();
        this.userRepository.findById(userrecuprer.getId()).ifPresent(user -> {
            user.setActif(true);
            this.userRepository.save(user);
        });
    }
}
