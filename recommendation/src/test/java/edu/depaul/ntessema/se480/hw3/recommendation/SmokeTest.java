package edu.depaul.ntessema.se480.hw3.recommendation;

import edu.depaul.ntessema.se480.hw3.recommendation.controller.RecommendationController;
import edu.depaul.ntessema.se480.hw3.recommendation.service.MovieService;
import edu.depaul.ntessema.se480.hw3.recommendation.service.RestClientUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private RecommendationController recommendationController;

    @Autowired
    private MovieService movieService;

    @Autowired
    private RestClientUserService restClientUserService;

    @Test
    public void recommendationControllerLoads() {
        assertThat(recommendationController).isNotNull();
    }

    @Test
    public void movieServiceLoads() {
        assertThat(movieService).isNotNull();
    }

    @Test
    public void restClientUserServiceLoads() {
        assertThat(restClientUserService).isNotNull();
    }
}
