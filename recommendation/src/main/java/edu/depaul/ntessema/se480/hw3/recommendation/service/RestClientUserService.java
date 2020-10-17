package edu.depaul.ntessema.se480.hw3.recommendation.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import edu.depaul.ntessema.se480.hw3.recommendation.model.User;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RestClientUserService implements UserService {

    private final RestTemplate restTemplate;

    @Autowired
    public RestClientUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallbackMethod")
    public User getAuthenticatedUserDetail(String authToken) {
        final String userDetailServiceUri = "http://localhost:8081/v1/user-detail";
        /*
         * REST Web Client
         */
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-token", authToken);
        ResponseEntity<User> responseEntity = restTemplate.exchange(
                userDetailServiceUri,
                HttpMethod.GET,
                new HttpEntity<User>(headers),
                User.class);
        return responseEntity.getBody();
    }

    public User fallbackMethod(final String authToken) {
        log.info("Fallback method is invoked.");
        return new User("", 7);
    }
}
