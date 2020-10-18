package edu.depaul.ntessema.se480.hw3.ui.service;

import edu.depaul.ntessema.se480.hw3.ui.model.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SimpleRecommendationService implements RecommendationService {

    private final RestTemplate restTemplate;

    @Autowired
    public SimpleRecommendationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Movie> getRecommendations(String token) {

        final String recommendationEndpoint = "http://localhost:8082/v1/recommend";

        try {
            HttpHeaders headers = new HttpHeaders();
            List<Movie> movies = new ArrayList<>();
            headers.set("x-auth-token", token);
            ResponseEntity<List<Movie>> responseEntities = restTemplate.exchange(
                    recommendationEndpoint,
                    HttpMethod.GET,
                    new HttpEntity<Void>(headers),
                    new ParameterizedTypeReference<List<Movie>>() {
                    }
            );
            if(responseEntities.hasBody() && responseEntities.getBody() != null) {
                movies = responseEntities.getBody();
            }
            return movies;
        } catch (RestClientException rce) {
            log.error("Recommendation Service Error");
            return new ArrayList<>();
        }
    }
}
