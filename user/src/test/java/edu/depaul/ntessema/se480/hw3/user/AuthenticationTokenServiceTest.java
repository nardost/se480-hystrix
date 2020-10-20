package edu.depaul.ntessema.se480.hw3.user;

import edu.depaul.ntessema.se480.hw3.user.model.User;
import edu.depaul.ntessema.se480.hw3.user.repository.UserRepository;
import edu.depaul.ntessema.se480.hw3.user.service.AuthenticationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationTokenServiceTest {

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    /**
     * Mock the data repository.
     */
    @MockBean
    private UserRepository userRepository;

    /**
     * Mock the data repository to return
     * made up users for three user names.
     */
    @BeforeEach
    public void mockDataAccessBehavior() {
        final String[][] users = new String[][] {
                { "kid", "5" },
                { "teen", "14" },
                { "adult", "25" }
        };
        for(String[] userDetail : users) {
            try {
                when(userRepository.findFirstByUsername(userDetail[0]))
                        .thenReturn(new User(
                                UUID.randomUUID().toString(),
                                userDetail[0],
                                userDetail[0],
                                Integer.parseInt(userDetail[1])));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "kid", "teen", "adult" })
    public void authenticationTokenServiceGeneratesCorrectBase64EncodedToken(String username) {
        final User user = userRepository.findFirstByUsername(username);
        final String authToken = authenticationTokenService.issueAuthenticationToken(user);
        final String decodedToken = new String(Base64.getDecoder().decode(authToken));
        final String[] details = decodedToken.split("\\.");

        assertThat(details[0]).isEqualTo(user.getId());
        assertThat(details[1]).isEqualTo(user.getUsername());
        assertThat(details[2]).isEqualTo(Integer.toString(user.getAge()));
    }
}
