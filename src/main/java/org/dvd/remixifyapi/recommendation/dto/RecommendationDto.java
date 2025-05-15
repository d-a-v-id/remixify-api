package org.dvd.remixifyapi.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationDto {
    private Long id;
    private String title;
    private double score;
    private List<String> commonIngredients;
    private String explanation;
}
