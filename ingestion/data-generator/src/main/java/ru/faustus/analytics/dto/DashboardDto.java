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
public class DashboardDto {
    private Long dailyActiveUsers;
    private Long monthlyActiveUsers;
    private Long totalEvents;
    private Long avgSessionDuration;
    private Double retentionRate;
    private Double conversionRate;
    private Map<String, Long> eventsByCategory;
    private Map<Integer, Long> userActivityByHour;
    private List<TopEventDto> topEvents;
    private List<GeoDistributionDto> geoDistribution;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopEventDto {
        private Long eventId;
        private String title;
        private Long views;
        private Long likes;
        private Double engagementScore;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeoDistributionDto {
        private String city;
        private Long usersCount;
        private Long eventsCount;
        private Double percentage;
    }
}