package ru.faustus.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnalyticsDto {
    private Long userId;
    private Long totalEvents;
    private Long uniqueCategories;
    private Long totalSessions;
    private Long avgSessionDuration;
    private List<String> favoriteCategories;
    private List<Map<String, Object>> activityTimeline;
    private Double engagementScore;
}