package ru.faustus.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для трендов по категориям.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendDto {
    private String category;
    private String trendName;
    private Long totalEvents;
    private Double growthRate;
    private String topEventType;
    private String topCity;
    private Double popularityScore;
    private Integer rank;
}