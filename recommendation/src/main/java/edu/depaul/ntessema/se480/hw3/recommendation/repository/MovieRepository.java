package edu.depaul.ntessema.se480.hw3.recommendation.repository;

import edu.depaul.ntessema.se480.hw3.recommendation.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
}
