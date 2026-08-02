package ru.faustus.analytics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.faustus.analytics.dto.*;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    public DashboardDto getDashboardMetrics(LocalDateTime start, LocalDateTime end,
                                            List<String> categories, String city) {
        log.info("Getting dashboard metrics for period: {} - {}", start, end);

        Map<String, Long> eventsByCategory = Map.ofEntries(
                Map.entry("Concert", 45_000L),
                Map.entry("Festival", 30_000L),
                Map.entry("Exhibition", 25_000L),
                Map.entry("Workshop", 20_000L),
                Map.entry("Conference", 15_000L),
                Map.entry("Other", 15_000L)
        );

        Map<Integer, Long> userActivityByHour = Map.ofEntries(
                Map.entry(8, 5_000L),
                Map.entry(9, 8_000L),
                Map.entry(10, 12_000L),
                Map.entry(11, 15_000L),
                Map.entry(12, 18_000L),
                Map.entry(13, 16_000L),
                Map.entry(14, 20_000L),
                Map.entry(15, 22_000L),
                Map.entry(16, 18_000L),
                Map.entry(17, 14_000L),
                Map.entry(18, 10_000L),
                Map.entry(19, 8_000L)
        );

        return DashboardDto.builder()
                .dailyActiveUsers(42_500L)
                .monthlyActiveUsers(1_250_000L)
                .totalEvents(150_000L)
                .avgSessionDuration(420L)
                .retentionRate(0.45)
                .conversionRate(0.12)
                .eventsByCategory(eventsByCategory)
                .userActivityByHour(userActivityByHour)
                .topEvents(List.of(
                        DashboardDto.TopEventDto.builder()
                                .eventId(1L)
                                .title("Spring Music Festival")
                                .views(12_000L)
                                .likes(3_500L)
                                .engagementScore(0.85)
                                .build(),
                        DashboardDto.TopEventDto.builder()
                                .eventId(2L)
                                .title("Tech Conference 2026")
                                .views(8_500L)
                                .likes(2_100L)
                                .engagementScore(0.75)
                                .build()
                ))
                .geoDistribution(List.of(
                        DashboardDto.GeoDistributionDto.builder()
                                .city("Moscow")
                                .usersCount(15_000L)
                                .eventsCount(45_000L)
                                .percentage(35.0)
                                .build(),
                        DashboardDto.GeoDistributionDto.builder()
                                .city("Saint Petersburg")
                                .usersCount(8_000L)
                                .eventsCount(24_000L)
                                .percentage(18.0)
                                .build()
                ))
                .build();
    }

    public RealtimeMetricsDto getRealtimeMetrics() {
        log.info("Getting real-time metrics");

        return RealtimeMetricsDto.builder()
                .activeUsers(4_250L)
                .eventsPerSecond(1_250L)
                .totalEventsToday(1_500_000L)
                .avgResponseTime(45.0)
                .systemLoad(0.65)
                .topEvents(List.of(
                        Map.of("eventId", 1L, "title", "Spring Music Festival", "views", 1234),
                        Map.of("eventId", 2L, "title", "Tech Conference 2026", "views", 987),
                        Map.of("eventId", 3L, "title", "Art Exhibition", "views", 654)
                ))
                .build();
    }

    public EventAnalyticsDto getEventAnalytics(Long eventId, LocalDateTime start, LocalDateTime end) {
        log.info("Getting analytics for event: {}", eventId);

        return EventAnalyticsDto.builder()
                .eventId(eventId)
                .eventTitle("Spring Music Festival")
                .category("Concert")
                .views(12_345L)
                .likes(3_456L)
                .registrations(2_789L)
                .comments(1_234L)
                .shares(567L)
                .purchases(890L)
                .conversionRate(0.22)
                .engagementScore(0.85)
                .dailyStats(List.of(
                        Map.of("date", "2026-01-01", "views", 1200, "likes", 300),
                        Map.of("date", "2026-01-02", "views", 1500, "likes", 450),
                        Map.of("date", "2026-01-03", "views", 1800, "likes", 600)
                ))
                .userDemographics(Map.of(
                        "18-25", 25L,
                        "26-35", 35L,
                        "36-50", 25L,
                        "50+", 15L
                ))
                .deviceDistribution(Map.of(
                        "MOBILE", 60L,
                        "DESKTOP", 30L,
                        "TABLET", 10L
                ))
                .cityDistribution(Map.of(
                        "Moscow", 45L,
                        "Saint Petersburg", 25L,
                        "Novosibirsk", 15L,
                        "Other", 15L
                ))
                .build();
    }

    public UserAnalyticsDto getUserAnalytics(Long userId, LocalDateTime start, LocalDateTime end) {
        log.info("Getting analytics for user: {}", userId);

        return UserAnalyticsDto.builder()
                .userId(userId)
                .userName("User " + userId)
                .totalEvents(1_234L)
                .uniqueCategories(5L)
                .totalSessions(45L)
                .avgSessionDuration(360L)
                .favoriteCategories(List.of("Concert", "Festival", "Exhibition"))
                .activityTimeline(List.of(
                        Map.of("date", "2026-01-01", "events", 45, "sessions", 3),
                        Map.of("date", "2026-01-02", "events", 67, "sessions", 5),
                        Map.of("date", "2026-01-03", "events", 52, "sessions", 4)
                ))
                .engagementScore(0.78)
                .eventTypeDistribution(Map.of(
                        "VIEW", 45L,
                        "LIKE", 20L,
                        "REGISTER", 15L,
                        "COMMENT", 10L,
                        "PURCHASE", 10L
                ))
                .deviceUsage(Map.of(
                        "MOBILE", 65L,
                        "DESKTOP", 30L,
                        "TABLET", 5L
                ))
                .build();
    }

    public List<TrendDto> getTrends(LocalDateTime start, LocalDateTime end, int limit) {
        log.info("Getting trends for period: {} - {}", start, end);

        return List.of(
                TrendDto.builder()
                        .category("Concert")
                        .trendName("Summer Music Festival")
                        .totalEvents(12_345L)
                        .growthRate(15.5)
                        .topEventType("VIEW")
                        .topCity("Moscow")
                        .popularityScore(0.92)
                        .rank(1)
                        .build(),
                TrendDto.builder()
                        .category("Tech")
                        .trendName("AI Conference")
                        .totalEvents(8_765L)
                        .growthRate(22.3)
                        .topEventType("REGISTER")
                        .topCity("Saint Petersburg")
                        .popularityScore(0.85)
                        .rank(2)
                        .build(),
                TrendDto.builder()
                        .category("Exhibition")
                        .trendName("Modern Art")
                        .totalEvents(6_543L)
                        .growthRate(8.7)
                        .topEventType("LIKE")
                        .topCity("Moscow")
                        .popularityScore(0.78)
                        .rank(3)
                        .build()
        );
    }

    public FunnelDto getConversionFunnel(LocalDateTime start, LocalDateTime end, Long eventId) {
        log.info("Getting conversion funnel for period: {} - {}", start, end);

        return FunnelDto.builder()
                .steps(List.of("VIEW", "REGISTER", "LIKE", "COMMENT", "PURCHASE"))
                .counts(List.of(100_000L, 25_000L, 15_000L, 8_000L, 2_500L))
                .conversionRates(List.of(100.0, 25.0, 60.0, 53.3, 31.25))
                .dropOffRates(List.of(0.0, 75.0, 40.0, 46.7, 68.75))
                .totalUsers(100_000L)
                .overallConversionRate(2.5)
                .build();
    }
}