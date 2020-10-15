package edu.depaul.ntessema.se480.hw3.recommendation.service;

import edu.depaul.ntessema.se480.hw3.recommendation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class RestClientUserService implements UserService {

    private final RestTemplate restTemplate;

    @Autowired
    public RestClientUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public User getAuthenticatedUserDetail(String authToken) {
        final String AUTH_USER_ENDPOINT = "http://localhost:8081/v1/user-detail";
        /*
         * REST Web Client
         */
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-auth-token", authToken);
            ResponseEntity<User> responseEntity = restTemplate.exchange(
                    AUTH_USER_ENDPOINT,
                    HttpMethod.GET,
                    new HttpEntity<User>(headers),
                    User.class);
            final User user = responseEntity.getBody();
            if (Objects.isNull(user)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return responseEntity.getBody();
        } catch (RestClientException | ResponseStatusException exception) {
            // TODO: what to do if user is not authenticated?
            return new User("", 7);
        }
    }
}
