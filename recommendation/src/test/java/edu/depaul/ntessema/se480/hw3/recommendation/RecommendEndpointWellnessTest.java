package edu.depaul.ntessema.se480.hw3.recommendation;

import edu.depaul.ntessema.se480.hw3.recommendation.model.Movie;
import edu.depaul.ntessema.se480.hw3.recommendation.model.User;
import edu.depaul.ntessema.se480.hw3.recommendation.service.RecommendationService;
import edu.depaul.ntessema.se480.hw3.recommendation.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecommendEndpointWellnessTest {

    @Autowired
    private MockMvc mockMvc;

    private List<Movie> movies;

    @BeforeEach
    public void init() {
        movies = new ArrayList<>();
        final String[][] MOVIES = new String[][]{
                {"Shrek", "0", "13"},
                {"Coco", "0", "13"},
                {"The Incredibles", "0", "13"},
                {"The Avengers", "13", "17"},
                {"The Dark Knight", "13", "17"},
                {"Inception", "13", "17"},
                {"The Godfather", "17", "120"},
                {"Deadpool", "17", "120"},
                {"Saving Private Ryan", "17", "120"}
        };
        Stream.of(MOVIES)
                .map(m -> new Movie(
                        UUID.randomUUID().toString(),
                        m[0],
                        Integer.parseInt(m[1]),
                        Integer.parseInt(m[2])))
                .forEach(movies::add);
    }

    @Test
    public void recommendEndpointIsLiveAndRespondsOk() throws Exception {
        final String fakeAuthToken = "some-fake-authentication-token";
        mockMvc.perform(
                get("/v1/recommend")
                .header("x-auth-token", fakeAuthToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Coco")));
    }
}
