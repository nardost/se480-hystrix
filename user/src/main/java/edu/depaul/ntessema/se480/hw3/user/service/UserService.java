package edu.depaul.ntessema.se480.hw3.user.service;

import edu.depaul.ntessema.se480.hw3.user.model.User;
import edu.depaul.ntessema.se480.hw3.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public User findByUsername(String username) {
        User user = repository.findFirstByUsername(username);
        if(user == null) {
            log.error("User not found: " + username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }
}
