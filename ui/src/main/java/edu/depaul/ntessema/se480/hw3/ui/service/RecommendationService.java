package edu.depaul.ntessema.se480.hw3.ui.service;

import edu.depaul.ntessema.se480.hw3.ui.model.Movie;

import java.util.List;

public interface RecommendationService {
    List<Movie> getRecommendations(String token);
}
