package edu.depaul.ntessema.se480.hw3.recommendation.repository;

import edu.depaul.ntessema.se480.hw3.recommendation.model.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@Slf4j
public class MovieLoader {

    private final MovieRepository repository;

    @Autowired
    public MovieLoader(MovieRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void load() {
        Stream.of(MOVIES).forEach(m -> {
            try {
                Movie movie = new Movie(
                        UUID.randomUUID().toString(),
                        m[0],
                        Integer.parseInt(m[1]),
                        Integer.parseInt(m[2]));
                repository.save(movie);
            } catch (IllegalArgumentException ignored) {
                log.error(String.format("%s is not saved. Age must be 0 or a positive integer.", m[0]));
            }
        });
    }

    /**
     * List of movies with their age rating.
     */
    private static final String[][] MOVIES = new String[][] {
            { "Shrek", "0", "13" },
            { "Coco", "0", "13" },
            { "The Incredibles", "0", "13" },
            { "The Avengers", "13", "17" },
            { "The Dark Knight", "13", "17" },
            { "Inception", "13", "17" },
            { "The Godfather", "17", "120" },
            { "Deadpool", "17", "120" },
            { "Saving Private Ryan", "17", "120" },
            // more kids' movies
            { "Shark Tale", "0", "13" },
            { "The Lion King", "0", "13" },
            // more teens movies
            { "Harry Potter", "13", "17" },
            { "Jurassic Park", "13", "17" },
            { "Avatar", "13", "17" },
            // more adults' movies
            { "Casablanca", "17", "120" },
            { "Play It Again Sam", "17", "120" },
            { "Schindler's List", "17", "120" },
            { "Inglourious Basterds", "17", "120" }
    };
}
