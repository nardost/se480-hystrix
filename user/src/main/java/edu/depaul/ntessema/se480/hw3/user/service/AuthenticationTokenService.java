package edu.depaul.ntessema.se480.hw3.user.service;

import edu.depaul.ntessema.se480.hw3.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class AuthenticationTokenService {

    /**
     * Store issued authentication tokens here.
     *   key = authentication token
     * value = user id
     */
    private final Map<String, String> issuedTokens = new HashMap<>();
    /**
     * The data access abstraction.
     */
    private final UserService userService;

    @Autowired
    public AuthenticationTokenService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Generate a JWT like authentication token for a user.
     * Concatenates id, username, age separated by a dot (.)
     * and encodes the result in base 64 text encoding.
     * Since the id is a UUID string, a user will have
     * different auth tokens across service restarts.
     *
     * @param user the user for whom auth token is generated
     * @return the authentication token string.
     */
    public String issueAuthenticationToken(final User user) {

        final byte[] bytes = user.getId()
                .concat(".")
                .concat(user.getUsername())
                .concat(".")
                .concat(Integer.toString(user.getAge()))
                .getBytes();
        final String authToken = Base64.getEncoder().encodeToString(bytes);
        /*
         * If user has been issued a token
         * previously, just return the old token.
         * Otherwise, add an entry to the issued
         * tokens map and return the token.
         */
        if(!issuedTokens.containsKey(authToken)) {
            issuedTokens.put(authToken, user.getId());
            log.info(String.format(
                    "New authentication token generated for user %s: %s%n",
                    user.getUsername(),
                    authToken));
        }
        return authToken;
    }

    /**
     * @param authToken the user authentication token.
     * @return user detail - in this case, just the age.
     */
    public int getAge(final String authToken) {
        final String id = issuedTokens.get(authToken);
        if(Objects.isNull(id)) {
            log.error("No user found that matches the authentication token: " + authToken);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userService.findById(id).getAge();
    }
}
