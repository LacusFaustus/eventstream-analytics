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
public class EventAnalyticsDto {
    private Long eventId;
    private Long views;
    private Long likes;
    private Long registrations;
    private Long comments;
    private Long shares;
    private Double conversionRate;
    private Double engagementScore;
    private List<Map<String, Object>> dailyStats;
    private Map<String, Long> userDemographics;
}