package edu.depaul.ntessema.se480.hw3.ui.service;

import edu.depaul.ntessema.se480.hw3.ui.model.User;

public interface AuthenticationService {
    String authenticate(User user);
}
