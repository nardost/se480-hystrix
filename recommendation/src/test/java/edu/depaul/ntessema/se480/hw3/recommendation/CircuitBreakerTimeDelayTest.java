package edu.depaul.ntessema.se480.hw3.recommendation;

import edu.depaul.ntessema.se480.hw3.recommendation.model.User;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CircuitBreakerTimeDelayTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock the RestTemplate REST client.
     */
    @MockBean
    private RestTemplate restTemplate;

    private final static String authToken = "fake-authentication-token";

    @ParameterizedTest
    @ValueSource(longs = { 0L, 10L, 20L, 40L, 50L })
    public void whenUserServiceDelayIsLessThanThresholdCircuitIsClosedAndResponseIsNormal(long delay)
            throws Exception {

        /*
         * The response is an adult user with time delay < 100ms
         */
        mockTimeDelayInUserServiceAndSendAdultUserInResponse(delay);
        /*
         * The time delay is below the threshold (100ms). So the circuit should
         * stay closed, and the call should return movies for adults.
         */
        mockMvc.perform(get("/v1/recommend").header("x-auth-token", authToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("Deadpool")));
    }

    /**
     * Values close to 100 resulted in test failures a couple of times.
     * I could not reproduce that and don't still understand why that happened.
     * I am, therefore, picking delays far bigger than 100 to be safe for this HW.
     */
    @ParameterizedTest
    @ValueSource(longs = { 150L, 175L, 200L })
    public void whenUserServiceDelayExceedsThresholdCircuitBreaksAndResponseIsKidsRatedMovies(long delay)
            throws Exception {

        /*
         * The response is an adult user with some time delay > 100ms
         */
        mockTimeDelayInUserServiceAndSendAdultUserInResponse(delay);
        /*
         * Delay exceeds the threshold (100ms). So the circuit breaker
         * should open the circuit and the call should return
         * movies rated for kids.
         */
        mockMvc.perform(get("/v1/recommend").header("x-auth-token", authToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("Coco")));
    }

    /**
     * Mock a user service that responds with some time delay.
     */
    private void mockTimeDelayInUserServiceAndSendAdultUserInResponse(long delay) {
        final String userDetailUri = "http://localhost:8081/v1/user-detail";
        final HttpHeaders headers = new HttpHeaders();
        /*
         * A study note:
         *
         * The headers must be set BEFORE the HttpEntity is created
         * because HttpEntity constructor creates a new HttpHeaders object:
         * HttpHeaders tempHeaders = new HttpHeaders();
         * if (headers != null) {
         *   tempHeaders.putAll(headers);
         * }
         */
        headers.set("x-auth-token", authToken);
        final HttpEntity<User> httpEntity = new HttpEntity<>(headers);
        final User adultUser = new User("adult", 25);
        /*
         * Respond with time delay. Adult user is in the response.
         */
        when(restTemplate.exchange(userDetailUri, HttpMethod.GET, httpEntity, User.class))
                .thenAnswer((Answer<ResponseEntity<User>>) invocationOnMock -> {
                    TimeUnit.MILLISECONDS.sleep(delay);
                    return new ResponseEntity<>(adultUser, HttpStatus.OK);
                });
    }
}
