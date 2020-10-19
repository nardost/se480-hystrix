package edu.depaul.ntessema.se480.hw3.recommendation.service;

import edu.depaul.ntessema.se480.hw3.recommendation.model.User;

/**
 * An interface for the user service. This makes the
 * user service "mockable" for testing.
 */
public interface UserService {
    User getAuthenticatedUserDetail(String authToken);
}
