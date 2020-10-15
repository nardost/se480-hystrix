package edu.depaul.ntessema.se480.hw3.recommendation.service;

import edu.depaul.ntessema.se480.hw3.recommendation.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final MovieService movieService;
    private final UserService userService;

    @Autowired
    public RecommendationService(MovieService service, UserService userService) {
        this.movieService = service;
        this.userService = userService;
    }

    public List<Movie> getRecommendedMovies(final String authToken) {
        return movieService.findByAgeGroup(userService.getAuthenticatedUserDetail(authToken).getAge());
    }
}
