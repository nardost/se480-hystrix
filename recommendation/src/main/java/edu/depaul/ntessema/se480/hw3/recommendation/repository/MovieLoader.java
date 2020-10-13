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
                log.error(String.format("%s is not saved. Ages must be integers.", m[0]));
            }
        });
    }

    private static final String[][] MOVIES = new String[][] {
            { "Shrek", "0", "13" },
            { "Coco", "0", "13" },
            { "The Incredibles", "0", "13" },
            { "Shark Tale", "0", "13" },
            { "The Lion King", "0", "13" },
            { "The Avengers", "13", "17" },
            { "The Dark Knight", "13", "17" },
            { "Inception", "13", "17" },
            { "The Godfather", "17", "120" },
            { "Deadpool", "17", "120" },
            { "Saving Private Ryan", "17", "120" }
    };
}
