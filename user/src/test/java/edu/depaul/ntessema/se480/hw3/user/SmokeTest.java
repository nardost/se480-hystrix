package edu.depaul.ntessema.se480.hw3.user;

import edu.depaul.ntessema.se480.hw3.user.repository.UserRepository;
import edu.depaul.ntessema.se480.hw3.user.service.AuthenticationTokenService;
import edu.depaul.ntessema.se480.hw3.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @Test
    public void userRepositoryLoads() {
        assertThat(userRepository).isNotNull();
    }

    @Test
    public void userServiceLoads() {
        assertThat(userService).isNotNull();
    }

    @Test
    public void authenticationTokenServiceLoads() {
        assertThat(authenticationTokenService).isNotNull();
    }
}
