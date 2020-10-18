package edu.depaul.ntessema.se480.hw3.recommendation;

import edu.depaul.ntessema.se480.hw3.recommendation.model.User;
import org.junit.jupiter.api.BeforeEach;
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
public class UserServiceTimeDelayTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    private User adultUser;
    private HttpEntity<User> httpEntity;
    private String authToken;
    private String userDetailUri;

    @BeforeEach
    public void init() {
        authToken = "fake-authentication-token";
        userDetailUri = "http://localhost:8081/v1/user-detail";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-token", authToken);
        httpEntity = new HttpEntity<>(headers);
        adultUser = new User("adult", 25);
    }

    @ParameterizedTest
    @ValueSource(longs = { 0L, 10L, 20L, 40L, 50L })
    public void whenUserServiceDelayIsLessThanThresholdRecommenderCircuitIsClosed(long delay) throws Exception {

        mockDelayedUserServiceResponse(delay);

        mockMvc.perform(get("/v1/recommend").header("x-auth-token", authToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("Deadpool")));
    }

    @ParameterizedTest
    @ValueSource(longs = { 100L, 125L, 150L, 175L, 200L })
    public void whenUserServiceDelayExceedsThresholdRecommenderCircuitOpens(long delay) throws Exception {

        mockDelayedUserServiceResponse(delay);

        mockMvc.perform(get("/v1/recommend").header("x-auth-token", authToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("Coco")));
    }

    private void mockDelayedUserServiceResponse(long delay) {
        when(restTemplate.exchange(userDetailUri, HttpMethod.GET, httpEntity, User.class))
                .thenAnswer((Answer<ResponseEntity<User>>) invocationOnMock -> {
                    TimeUnit.MILLISECONDS.sleep(delay);
                    return new ResponseEntity<>(adultUser, HttpStatus.OK);
                });
    }
}
