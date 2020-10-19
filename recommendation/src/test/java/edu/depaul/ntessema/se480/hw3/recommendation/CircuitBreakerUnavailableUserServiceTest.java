package edu.depaul.ntessema.se480.hw3.recommendation;

import edu.depaul.ntessema.se480.hw3.recommendation.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.containsString;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * If the user service is active while this test is being run,
 * and if the authentication token is a valid token, then the
 * user service will send a valid response to the test under execution.
 *
 * Therefore, the user service has to be mocked to make sure that whenever
 * this test is run a call to the user service always results in a 404 response.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CircuitBreakerUnavailableUserServiceTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock the RestTemplate REST client
     */
    @MockBean
    private RestTemplate restTemplate;
    /**
     * an authentication token (could work if there is a live user service)
     */
    private final String authToken = "NzM0ZGNjOGYtZjJkOS00YmVjLTk3MGQtMTM4ZDBhMWI0MGZkLm5hcmRvcy4yMA==";

    @Test
    public void whenUserServiceIsNotAvailableRecommenderCircuitBreaksAndResponseIsKidsRatedMovies()
            throws Exception {
        /*
         * The response is a RestClientException (404)
         */
        mockUnavailableUserService();
        /*
         * mock a REST call to /v1/recommend and assert the results...
         */
        mockMvc.perform(get("/v1/recommend").header("x-auth-token", authToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Coco")));
    }

    /**
     * The following mock simulates unavailable user service by
     * throwing a RestClientException when the user service endpoint
     * /v1/user-detail is invoked.
     *
     * The exception is equivalent to a 404 (not found)
     * response, which triggers the circuit breaker.
     */
    private void mockUnavailableUserService() {

        final String userDetailUri = "http://localhost:8081/v1/user-detail";

        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-token", authToken);
        final HttpEntity<User> httpEntity = new HttpEntity<>(headers);
        /*
         * Mock a user service that is unavailable.
         */
        when(restTemplate.exchange(userDetailUri, HttpMethod.GET, httpEntity, User.class))
                .thenThrow(new RestClientException("simulating a broken user service - 404"));
    }
}
