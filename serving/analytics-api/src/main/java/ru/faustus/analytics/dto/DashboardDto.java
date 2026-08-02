package ru.faustus.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO для дашборда с ключевыми бизнес-метриками.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {

    // ============================================================
    // КЛЮЧЕВЫЕ МЕТРИКИ
    // ============================================================

    private Long dailyActiveUsers;        // DAU
    private Long monthlyActiveUsers;      // MAU
    private Long totalEvents;             // Всего событий за период
    private Long avgSessionDuration;      // Средняя длительность сессии (мс)

    // ============================================================
    // БИЗНЕС-МЕТРИКИ
    // ============================================================

    private Double retentionRate;         // Удержание пользователей (%)
    private Double conversionRate;        // Конверсия (%)
    private Double churnRate;             // Отток (%)

    // ============================================================
    // ДЕТАЛИЗАЦИЯ
    // ============================================================

    private Map<String, Long> eventsByCategory;     // События по категориям
    private Map<Integer, Long> userActivityByHour;  // Активность по часам

    // ============================================================
    // ТОПЫ
    // ============================================================

    private List<TopEventDto> topEvents;            // Топ событий
    private List<GeoDistributionDto> geoDistribution; // Гео-распределение
    private List<DeviceDistributionDto> deviceDistribution; // Распределение по устройствам

    // ============================================================
    // ДИНАМИКА
    // ============================================================

    private List<DailyMetricDto> dailyTrends;       // Ежедневная динамика

    // ============================================================
    // ВНУТРЕННИЕ DTO
    // ============================================================

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceDistributionDto {
        private String device;
        private Long count;
        private Double percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyMetricDto {
        private String date;
        private Long activeUsers;
        private Long newUsers;
        private Long totalEvents;
    }
}