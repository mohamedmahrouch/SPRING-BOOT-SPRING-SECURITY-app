package com.example.demo.controller;

import com.example.demo.model.User2;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public User2 inscription(@RequestBody User2 user) {
    log.info("....... informations des user par postman: {}", user);

    try {
        return userService.createUser(user);
    } catch (Exception e) {
        log.error("Erreur lors de la création de l'utilisateur au base de données ", e);
        throw e;
    }
    }
    @PostMapping("/verification")
    public void verification(@RequestBody Map<String,String> activation) {
        this.userService.verification(activation);

    }
    @GetMapping
    public List<User2> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User2> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }
  @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User2 createUser(@RequestBody User2 user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User2 updateUser(@PathVariable String id, @RequestBody User2 user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
    @GetMapping("/by-email/{email}")
    public ResponseEntity<List<User2>> getByEmail(@PathVariable String email) {
        List<User2> users = userService.findusers(email);
        return users.isEmpty() ? ResponseEntity.notFound().build() : (ResponseEntity<List<User2>>) ResponseEntity.ok();
    }
    @GetMapping("/byname")
    public List<User2> recupere(@RequestParam String name) {
        return userService.recupereuser(name);
    }

}
