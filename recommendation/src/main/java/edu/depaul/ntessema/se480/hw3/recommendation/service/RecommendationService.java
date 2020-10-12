package edu.depaul.ntessema.se480.hw3.recommendation.service;

import edu.depaul.ntessema.se480.hw3.recommendation.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final MovieService movieService;

    @Autowired
    public RecommendationService(MovieService service) {
        this.movieService = service;
    }

    public List<Movie> getRecommendedMovies(final String authToken) {
        /*
         * TODO
         *  1. Request: user service with token.
         *  2. Response: user details based on auth token.
         *  3. Extract age from user details
         */
        final int age = Integer.parseInt(authToken.split("\\.")[1]);
        return movieService.findByAgeGroup(age);
    }
}
