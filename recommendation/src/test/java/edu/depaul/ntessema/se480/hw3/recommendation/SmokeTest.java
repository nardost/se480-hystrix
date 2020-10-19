package edu.depaul.ntessema.se480.hw3.recommendation;

import edu.depaul.ntessema.se480.hw3.recommendation.controller.RecommendationController;
import edu.depaul.ntessema.se480.hw3.recommendation.repository.MovieRepository;
import edu.depaul.ntessema.se480.hw3.recommendation.service.MovieService;
import edu.depaul.ntessema.se480.hw3.recommendation.service.RecommendationService;
import edu.depaul.ntessema.se480.hw3.recommendation.service.RestClientUserService;
import edu.depaul.ntessema.se480.hw3.recommendation.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestClientUserService restClientUserService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private RecommendationController recommendationController;

    @Test
    public void movieRepositoryLoads() {
        assertThat(movieRepository).isNotNull();
    }

    @Test
    public void movieServiceLoads() {
        assertThat(movieService).isNotNull();
    }

    @Test
    public void userServiceLoads() {
        assertThat(userService).isNotNull();
    }

    @Test
    public void restClientUserServiceLoads() {
        assertThat(restClientUserService).isNotNull();
    }

    @Test
    public void recommendationServiceLoads() {
        assertThat(recommendationService).isNotNull();
    }

    @Test
    public void recommendationControllerLoads() {
        assertThat(recommendationController).isNotNull();
    }
}
