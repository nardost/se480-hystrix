package edu.depaul.ntessema.se480.hw3.ui;

import edu.depaul.ntessema.se480.hw3.ui.service.AuthenticationService;
import edu.depaul.ntessema.se480.hw3.ui.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RecommendationService recommendationService;

    @Test
    public void authenticationServiceLoads() {
        assertThat(authenticationService).isNotNull();
    }

    @Test
    public void recommendationServiceLoads() {
        assertThat(recommendationService).isNotNull();
    }
}
