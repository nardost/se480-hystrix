package edu.depaul.ntessema.se480.hw3.recommendation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
public class Movie {
    @Id
    private String id;
    private String title;
    private int minimumAge;
    private int maximumAge;
}
