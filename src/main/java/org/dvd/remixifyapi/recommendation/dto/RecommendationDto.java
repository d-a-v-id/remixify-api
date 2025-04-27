package org.dvd.remixifyapi.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendationDto {
    private Long id;
    private String title;
    private double score;
}
