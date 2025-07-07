package org.dvd.remixifyapi.recommendation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecommendationResponse {
    private List<RecommendationDto> recommendations;
    private Stats stats;
    private String explanation;
}
