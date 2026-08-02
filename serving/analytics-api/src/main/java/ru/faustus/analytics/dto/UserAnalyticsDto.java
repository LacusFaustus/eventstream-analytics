package ru.faustus.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO для поведенческой аналитики пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnalyticsDto {
    private Long userId;
    private String userName;
    private Long totalEvents;
    private Long uniqueCategories;
    private Long totalSessions;
    private Long avgSessionDuration;
    private List<String> favoriteCategories;
    private List<Map<String, Object>> activityTimeline;
    private Double engagementScore;
    private Map<String, Long> eventTypeDistribution;
    private Map<String, Long> deviceUsage;
}