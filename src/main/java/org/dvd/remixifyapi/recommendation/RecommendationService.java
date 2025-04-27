package org.dvd.remixifyapi.recommendation;

import org.dvd.remixifyapi.recommendation.dto.RecommendationResponse;

public interface RecommendationService {
    RecommendationResponse recommend(String username, int limit);
}
