package edu.depaul.ntessema.se480.hw3.ui.controller;

import edu.depaul.ntessema.se480.hw3.ui.model.Movie;
import edu.depaul.ntessema.se480.hw3.ui.model.User;
import edu.depaul.ntessema.se480.hw3.ui.service.RecommendationService;
import edu.depaul.ntessema.se480.hw3.ui.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class UIController {

    @Value("${spring.application.name}")
    private String applicationName;

    private final AuthenticationService userService;
    private final RecommendationService recommendationService;

    @Autowired
    public UIController(
            AuthenticationService userService,
            RecommendationService recommendationService) {
        this.userService = userService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/")
    public String home(Model model) {
        /*
         * TODO
         *  The flash attribute redirected
         *  from the POST endpoint /recommend
         *  can be retrieved as follows:
         *  model.asMap().get("movies")
         */
        model.addAttribute("applicationName", applicationName);
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/recommend")
    public String recommendMovies(
            @ModelAttribute User user,
            RedirectAttributes redirectAttributes) {
        final String authToken = userService.authenticate(user);
        final List<Movie> recommendedMovies = recommendationService.getRecommendations(authToken);
        /*
         * Define theme for the recommendation category.
         * Get first movie and use the max age, (or maxAge = 0 if no movies are there).
         */
        final int maximumAge = recommendedMovies.stream().findFirst().orElse(new Movie()).getMaximumAge();
        final String theme = maximumAge <= 13 ? "kids" : maximumAge == 17 ? "teens" : "adults";
        /*
         * PRG redirection: POST, Redirect, GET to avoid
         * accidentally rePOSTing on browser refresh.
         * Use RedirectAttributes and add flash attributes
         * that last only for the duration of the redirection.
         * RedirectAttributes is a Model, so the next controller
         * mapping will get the movies attribute...
         */
        redirectAttributes.addFlashAttribute("theme", theme);
        redirectAttributes.addFlashAttribute("movies", recommendedMovies);
        return "redirect:/";
    }
}
