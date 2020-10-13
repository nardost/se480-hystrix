package edu.depaul.ntessema.se480.hw3.ui.controller;

import edu.depaul.ntessema.se480.hw3.ui.model.Movie;
import edu.depaul.ntessema.se480.hw3.ui.model.User;
import edu.depaul.ntessema.se480.hw3.ui.service.RecommendationService;
import edu.depaul.ntessema.se480.hw3.ui.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class UIController {

    @Value("${spring.application.name}")
    private String applicationName;

    private final UserService userService;
    private final RecommendationService recommendationService;

    @Autowired
    public UIController(UserService userService, RecommendationService recommendationService) {
        this.userService = userService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("applicationName", applicationName);
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/recommend")
    public String recommendMovies(@ModelAttribute User user, Model model) {
        final String authToken = userService.authenticate(user);
        final List<Movie> recommendedMovies = recommendationService.getRecommendations(authToken);
        recommendedMovies.stream().map(Movie::getTitle).forEach(log::info);
        model.addAttribute("movies", recommendedMovies);
        return "index";
    }
}
