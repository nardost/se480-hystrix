package edu.depaul.ntessema.se480.hw3.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class Movie {
    private String title;
    private int minimumAge;
    private int maximumAge;
}
