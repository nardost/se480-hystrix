package edu.depaul.ntessema.se480.hw3.user.repository;

import edu.depaul.ntessema.se480.hw3.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
