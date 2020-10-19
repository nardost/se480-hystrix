package edu.depaul.ntessema.se480.hw3.recommendation.service;

import edu.depaul.ntessema.se480.hw3.recommendation.model.Movie;
import edu.depaul.ntessema.se480.hw3.recommendation.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository repository;

    @Autowired
    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    /**
     * Get the list of movies that match the age of user.
     *
     * @param age the age of the authenticated user.
     * @return the list of movies rated for that age.
     */
    public List<Movie> findByAgeGroup(int age) {
        return repository
                .findAll()
                .stream()
                .filter(movie -> movie.getMinimumAge() <= age && age < movie.getMaximumAge())
                .collect(Collectors.toList());
    }
}
