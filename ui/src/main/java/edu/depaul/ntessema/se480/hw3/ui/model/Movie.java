package edu.depaul.ntessema.se480.hw3.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
public class Movie {
    private String title;
    private int minimumAge;
    private int maximumAge;
}
