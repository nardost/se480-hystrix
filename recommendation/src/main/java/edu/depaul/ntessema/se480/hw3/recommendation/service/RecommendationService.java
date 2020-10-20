package edu.depaul.ntessema.se480.hw3.recommendation.service;

import edu.depaul.ntessema.se480.hw3.recommendation.model.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RecommendationService {

    /**
     * The local movie service is injected as a dependency.
     */
    private final MovieService movieService;
    /**
     * The remote user service is injected as a dependency.
     */
    private final UserService userService;

    @Autowired
    public RecommendationService(MovieService service, UserService userService) {
        this.movieService = service;
        this.userService = userService;
    }

    /**
     * Get user detail from the user service. Use the age to get
     * a list of recommended movies for that age group.
     *
     * @param authToken authentication token of user.
     * @return list of recommended movies.
     */
    public List<Movie> getRecommendedMovies(final String authToken) {
        /*
         * Contact the user service to ask for user detail.
         */
        final int userDetail = userService.getAuthenticatedUserDetail(authToken).getAge();
        log.info("User detail: age = " + userDetail);
        /*
         * Get the list of recommended movies from movieService and return them.
         */
        return movieService.findByAgeGroup(userDetail);
    }

    /**
     * For administration purposes only
     * @return list of all available movies
     */
    public List<Movie> getAllMovies() {
        return movieService.findAll();
    }
}
