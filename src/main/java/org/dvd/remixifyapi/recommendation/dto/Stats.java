package org.dvd.remixifyapi.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stats {
    private double maxScore;
    private double minScore;
    private double avgScore;
}
