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
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthenticationTokenService {

    /**
     * Store issued token here.
     */
    private final Map<String, String> issuedTokens = new HashMap<>();

    private final UserService userService;

    @Autowired
    public AuthenticationTokenService(UserService userService) {
        this.userService = userService;
    }

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
        }
        return authToken;
    }

    /**
     * @param authToken
     * @return
     */
    public int getAge(final String authToken) {
        final String id = issuedTokens.get(authToken);
        if(Objects.isNull(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userService.findById(id).getAge();
    }
}
