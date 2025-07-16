package org.dvd.remixifyapi.recommendation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendationDto {
    private Long id;
    private String title;
    private double score;
    private List<String> commonIngredients;
    private String explanation;
}
