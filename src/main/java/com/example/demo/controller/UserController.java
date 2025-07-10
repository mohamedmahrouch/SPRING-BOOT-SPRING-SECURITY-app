package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
@PostMapping("/inscription")
@ResponseStatus(HttpStatus.CREATED)
    public User inscription(@RequestBody User user) {
    log.info("Creating user: {}", user);
    try {
        return userService.createUser(user);
    } catch (Exception e) {
        log.error("Erreur lors de la création de l'utilisateur", e);
        throw e;
    }
    }
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }
  @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
    @GetMapping("/by-email/{email}")
    public ResponseEntity<List<User>> getByEmail(@PathVariable String email) {
        List<User> users = userService.findusers(email);
        return users.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(users);
    }
    @GetMapping("/byname")
    public List<User> recupere(@RequestParam String name) {
        return userService.recupereuser(name);
    }

}
