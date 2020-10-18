package edu.depaul.ntessema.se480.hw3.recommendation.service;

import edu.depaul.ntessema.se480.hw3.recommendation.model.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RecommendationService {

    private final MovieService movieService;
    private final UserService userService;

    @Autowired
    public RecommendationService(MovieService service, UserService userService) {
        this.movieService = service;
        this.userService = userService;
    }

    public List<Movie> getRecommendedMovies(final String authToken) {
        final int userDetail = userService.getAuthenticatedUserDetail(authToken).getAge();
        log.info("User detail: age = " + userDetail);
        return movieService.findByAgeGroup(userDetail);
    }
}
