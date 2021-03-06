package edu.depaul.ntessema.se480.hw3.recommendation.controller;

import edu.depaul.ntessema.se480.hw3.recommendation.model.Movie;
import edu.depaul.ntessema.se480.hw3.recommendation.service.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService service) {
        this.recommendationService = service;
    }

    /**
     * The movie recommender endpoint.
     * @param authToken the auth token of user
     * @return list of movies rated for user.
     */
    @GetMapping("/recommend")
    public ResponseEntity<List<Movie>> recommend(@RequestHeader("x-auth-token") final String authToken) {
        List<Movie> recommendedMovies = recommendationService.getRecommendedMovies(authToken);
        return new ResponseEntity<>(recommendedMovies, HttpStatus.OK);
    }

    /**
     * We don't need this for the homework. Just for the sake of
     * completeness ... Or for administration purposes.
     * @return list of all available movies.
     */
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(recommendationService.getAllMovies(), HttpStatus.OK);
    }
}
