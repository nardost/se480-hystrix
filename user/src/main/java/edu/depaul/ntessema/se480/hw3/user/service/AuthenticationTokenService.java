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

    private final Map<String, String> issuedTokens = new HashMap<>();

    private final UserService userService;

    @Autowired
    public AuthenticationTokenService(UserService userService) {
        this.userService = userService;
    }

    public String issueAuthenticationToken(final User user) {
        /*
         * TODO
         *  append expiration date on token
         */
        final byte[] bytes = user.getId()
                .concat(".")
                .concat(user.getUsername())
                .concat(".")
                .concat(Integer.toString(user.getAge()))
                .getBytes();
        final String authToken = Base64.getEncoder().encodeToString(bytes);
        /*
         * If user has been issued a token
         * previously just return the old token.
         * Otherwise, add an entry to the issued
         * tokens map and return the token.
         */
        if(!issuedTokens.containsKey(authToken)) {
            issuedTokens.put(authToken, user.getId());
        }
        return authToken;
    }

    public User getAuthenticatedUserDetail(final String authToken) {
        final String id = issuedTokens.get(authToken);
        if(Objects.isNull(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User u = userService.findById(id);
        /*
         * Don't send the password.
         * TODO: improve with DTO
         */
        return new User(u.getId(), u.getUsername(), "", u.getAge());
    }
}
