package edu.depaul.ntessema.se480.hw3.ui.service;

import edu.depaul.ntessema.se480.hw3.ui.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SimpleAuthenticationService implements AuthenticationService {

    private final RestTemplate restTemplate;

    @Autowired
    public SimpleAuthenticationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String authenticate(User user) {
        final String authenticationEndpoint = "http://localhost:8081/v1/auth";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            Map<String, String> json = new HashMap<>();
            json.put("username", user.getUsername());
            json.put("password", user.getPassword());
            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(json, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    authenticationEndpoint,
                    httpEntity,
                    String.class);
            return responseEntity.getHeaders().getFirst("x-auth-token");
        } catch (RestClientException rce) {
            log.error("Rest client failed...");
            return "error";
        }
    }
}
