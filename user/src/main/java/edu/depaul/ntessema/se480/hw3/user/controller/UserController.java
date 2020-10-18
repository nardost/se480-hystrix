package edu.depaul.ntessema.se480.hw3.user.controller;

import edu.depaul.ntessema.se480.hw3.user.model.User;
import edu.depaul.ntessema.se480.hw3.user.service.AuthenticationTokenService;
import edu.depaul.ntessema.se480.hw3.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;
    private final AuthenticationTokenService authTokenService;

    @Autowired
    public UserController(UserService userService, AuthenticationTokenService authTokenService) {
        this.userService = userService;
        this.authTokenService = authTokenService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authenticate(@RequestBody User u) {
        final String username = u.getUsername();
        final String password = u.getPassword();
        User user = userService.findByUsername(username);
        if(!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        final String authToken = authTokenService.issueAuthenticationToken(user);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("x-auth-token", authToken);
        return ResponseEntity.ok().headers(responseHeaders).body("");
    }

    @GetMapping("/user-detail")
    public ResponseEntity<User> authenticatedUserDetail(
            @RequestHeader("x-auth-token") final String authToken) {
        /*
         * Create a user object with all the details needed by the
         * recommendation service (currently, only age is needed).
         */
        User user = new User("", "", "", authTokenService.getAge(authToken));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
