package edu.depaul.ntessema.se480.hw3.user.repository;

import edu.depaul.ntessema.se480.hw3.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@Slf4j
public class UserLoader {

    private final UserRepository repository;

    @Autowired
    public UserLoader(UserRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void load() {
        Stream.of(USERS).forEach(u -> {
            try {
                User user = new User(
                        UUID.randomUUID().toString(),
                        u[0],
                        u[1],
                        Integer.parseInt(u[2]));
                repository.save(user);
            } catch (IllegalArgumentException ignored) {
                log.error(String.format("User %s not saved. Age must be an integer.", u[0]));
            }
        });
        log.info(repository.findAll().size() + " users loaded into the in-memory database");
    }

    private static final String[][] USERS = new String[][] {
            // { username, password, age }
            { "stewie", "stewie", "6"},
            { "meg", "meg", "12"},
            { "mark", "mark", "13"},
            { "carol", "carol", "16"},
            { "bob", "bob", "17" },
            { "linda", "linda", "30" }
    };
}
