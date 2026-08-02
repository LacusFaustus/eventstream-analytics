package ru.faustus.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO для детальной аналитики по событию.
 * Содержит все метрики по конкретному событию.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventAnalyticsDto {
    private Long eventId;
    private String eventTitle;
    private String category;
    private Long views;
    private Long likes;
    private Long registrations;
    private Long comments;
    private Long shares;
    private Long purchases;
    private Double conversionRate;
    private Double engagementScore;
    private List<Map<String, Object>> dailyStats;
    private Map<String, Long> userDemographics;
    private Map<String, Long> deviceDistribution;
    private Map<String, Long> cityDistribution;
}