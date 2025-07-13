package com.example.demo.controller;

import com.example.demo.dto.AuthentificationDTO;
import com.example.demo.model.User2;
import com.example.demo.security.JwtSecurity;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtSecurity jwtSecurity;



    public UserController(UserService userService, AuthenticationManager authenticationManager,JwtSecurity jwtSecurity) {
        this.userService = userService;
        this.jwtSecurity = jwtSecurity;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/inscription")
    @ResponseStatus(HttpStatus.CREATED)
    public User2 inscription(@RequestBody User2 user) {
        log.info("....... informations des user par postman: {}", user);
        try {
            return userService.createUser(user);
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'utilisateur", e);
            throw e;
        }
    }

    @PostMapping("/activation")
    public void verification(@RequestBody Map<String, String> activation) {
        this.userService.activer(activation);
    }

    @PostMapping("/connexion")
    public Map<String, String> connecter(@RequestBody AuthentificationDTO authentificationDTO) {
        log.info("Tentative de connexion pour l'utilisateur : {}", authentificationDTO.username());
        String username = authentificationDTO.username();
        String password = authentificationDTO.password();
        log.info("Username: {}, Password: {}", username);
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)
            );
      log.info("contenu de l'authentification : {}", authenticate);

       Map<String,String> jwt = jwtSecurity.generateT(authentificationDTO.username());

       log.info("Authentification réussie : {}", authenticate.isAuthenticated());

       Map<String, String> response = new HashMap<>();
       response.put("authenticated", "true");

       response.put("token", jwt.get("token"));
       return response;

        } catch (Exception e) {
            log.error("Échec de l'authentification pour l'utilisateur : {} - Cause: {}", authentificationDTO.username(), e.getMessage());

            Map<String, String> response = new HashMap<>();
            response.put("authenticated", "false");
            response.put("error", e.getMessage());
            return response;
        }
    }

    @GetMapping
    public List<User2> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User2> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User2 updateUser(@PathVariable String id, @RequestBody User2 user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
