package edu.depaul.ntessema.se480.hw3.user.controller;

import edu.depaul.ntessema.se480.hw3.user.model.User;
import edu.depaul.ntessema.se480.hw3.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<ResponseEntity<User>> getUsers() {
        List<ResponseEntity<User>> userEntities = new ArrayList<>();
        List<User> users = service.findAll();
        users.forEach(user -> userEntities.add(new ResponseEntity<>(user, HttpStatus.OK)));
        return userEntities;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = service.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authenticate(@RequestBody User u) {
        final String id = u.getId();
        final String password = u.getPassword();
        User user = service.findById(id);
        if(!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        final String authenticationToken = user.getId().concat(".").concat(Integer.toHexString(user.getAge()));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("x-auth-token", authenticationToken);
        return ResponseEntity.ok().headers(responseHeaders).body("");
    }
}
