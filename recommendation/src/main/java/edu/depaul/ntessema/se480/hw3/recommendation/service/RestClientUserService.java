package edu.depaul.ntessema.se480.hw3.recommendation.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import edu.depaul.ntessema.se480.hw3.recommendation.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RestClientUserService implements UserService {

    private final RestTemplate restTemplate;

    @Autowired
    public RestClientUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @see <a href="https://github.com/Netflix/Hystrix/wiki/Configuration"></a>
     *
     * @param authToken the authentication token of the user
     * @return the user object containing user details (age only, for now).
     */
    @Override
    @HystrixCommand(
            fallbackMethod = "getMadeUpUserUnder13YearsOfAge",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")
            }
    )
    public User getAuthenticatedUserDetail(String authToken) {

        final String userDetailServiceUri = "http://localhost:8081/v1/user-detail";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-auth-token", authToken);
        ResponseEntity<User> responseEntity = restTemplate.exchange(
                userDetailServiceUri,
                HttpMethod.GET,
                new HttpEntity<User>(headers),
                User.class);
        return responseEntity.getBody();
    }

    /**
     * This fallback method returns a made up user with age < 13
     * so that the recommendation service recommends movies that
     * are rated for kids under 13 years of age.
     *
     * @param authToken
     * @return a made up user with age < 13.
     */
    public User getMadeUpUserUnder13YearsOfAge(final String authToken) {
        log.error("User service failed. Fallback method is invoked.");
        return new User("somePhantomKid", 7);
    }
}
