package edu.depaul.ntessema.se480.hw3.recommendation;

import edu.depaul.ntessema.se480.hw3.recommendation.model.User;
import edu.depaul.ntessema.se480.hw3.recommendation.service.UserService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MockedUserServiceTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock the UserService
     */
    @MockBean
    private UserService userService;

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 })
    public void whenAgeIsLessThan13RecommendationServiceReturnsKidsRatedMovies(int age)
            throws Exception {

        final String kidAuthToken = "fake-kid-authentication-token";
        /*
         * Mock behavior of the user service - return a kid user for kid auth token.
         */
        when(userService.getAuthenticatedUserDetail(kidAuthToken))
                .thenReturn(new User(age + "-year-old-kid", age));
        /*
         * mock a client connection to the /v1/recommend endpoint and assert results...
         */
        mockMvc.perform(get("/v1/recommend").header("x-auth-token", kidAuthToken))
                .andDo(print())
                .andExpect(content().string(containsString("Coco")));
    }

    @ParameterizedTest
    @ValueSource(ints = { 13, 14, 15, 16 })
    public void whenAgeIs13AndAboveButLessThan17RecommendationServiceReturnsTeenRatedMovies(int age)
            throws Exception {

        final String teenAuthToken = "fake-teen-authentication-token";
        /*
         * Mock behavior of the user service - return a teen user for teen auth token.
         */
        when(userService.getAuthenticatedUserDetail(teenAuthToken))
                .thenReturn(new User(age + "-year-old-teen", age));
        /*
         * mock a client connection to the /v1/recommend endpoint and assert results...
         */
        mockMvc.perform(get("/v1/recommend").header("x-auth-token", teenAuthToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The Avengers")));
    }

    @ParameterizedTest
    @ValueSource(ints = { 17, 18, 20, 25, 50, 80, 100, 119 })
    public void whenAgeIs17AndAboveRecommendationServiceReturnsAdultRatedMovies(int age)
            throws Exception {

        final String adultAuthToken = "fake-adult-authentication-token";
        /*
         * Mock behavior of the user service - return a kid user for adult auth token.
         */
        when(userService.getAuthenticatedUserDetail(adultAuthToken))
                .thenReturn(new User(age + "-year-old-adult", age));
        /*
         * mock a client connection to the /v1/recommend endpoint and assert results...
         */
        mockMvc.perform(get("/v1/recommend").header("x-auth-token", adultAuthToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Deadpool")));
    }
}
