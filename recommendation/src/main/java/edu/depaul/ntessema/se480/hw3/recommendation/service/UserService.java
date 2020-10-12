package edu.depaul.ntessema.se480.hw3.recommendation.service;

import edu.depaul.ntessema.se480.hw3.recommendation.model.User;

public interface UserService {
    User getAuthenticatedUserDetail(String authToken);
}
