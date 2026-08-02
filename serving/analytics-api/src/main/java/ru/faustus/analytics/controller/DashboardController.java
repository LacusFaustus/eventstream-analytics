package ru.faustus.analytics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.faustus.analytics.dto.*;
import ru.faustus.analytics.service.AnalyticsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics API", description = "API для бизнес-аналитики")
public class DashboardController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @Cacheable(value = "dashboard", key = "#start + '_' + #end")
    @Operation(summary = "Получить метрики дашборда")
    public ResponseEntity<DashboardDto> getDashboard(
            @Parameter(description = "Начало периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,

            @Parameter(description = "Конец периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,

            @Parameter(description = "Фильтр по категориям")
            @RequestParam(required = false) List<String> categories,

            @Parameter(description = "Фильтр по городу")
            @RequestParam(required = false) String city) {

        log.info("📊 GET /api/analytics/dashboard: start={}, end={}, categories={}, city={}",
                start, end, categories, city);

        return ResponseEntity.ok(
                analyticsService.getDashboardMetrics(start, end, categories, city)
        );
    }

    @GetMapping("/realtime")
    @Operation(summary = "Получить метрики в реальном времени")
    public ResponseEntity<RealtimeMetricsDto> getRealtimeMetrics() {
        log.info("⚡ GET /api/analytics/realtime");
        return ResponseEntity.ok(analyticsService.getRealtimeMetrics());
    }

    @GetMapping("/events/{eventId}")
    @Operation(summary = "Получить аналитику по событию")
    public ResponseEntity<EventAnalyticsDto> getEventAnalytics(
            @Parameter(description = "ID события")
            @PathVariable Long eventId,

            @Parameter(description = "Начало периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,

            @Parameter(description = "Конец периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {

        log.info("📊 GET /api/analytics/events/{}", eventId);
        return ResponseEntity.ok(
                analyticsService.getEventAnalytics(eventId, start, end)
        );
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Получить аналитику по пользователю")
    public ResponseEntity<UserAnalyticsDto> getUserAnalytics(
            @Parameter(description = "ID пользователя")
            @PathVariable Long userId,

            @Parameter(description = "Начало периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,

            @Parameter(description = "Конец периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {

        log.info("📊 GET /api/analytics/users/{}", userId);
        return ResponseEntity.ok(
                analyticsService.getUserAnalytics(userId, start, end)
        );
    }

    @GetMapping("/trends")
    @Operation(summary = "Получить тренды")
    public ResponseEntity<List<TrendDto>> getTrends(
            @Parameter(description = "Начало периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,

            @Parameter(description = "Конец периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,

            @Parameter(description = "Количество трендов")
            @RequestParam(defaultValue = "10") int limit) {

        log.info("📈 GET /api/analytics/trends");
        return ResponseEntity.ok(
                analyticsService.getTrends(start, end, limit)
        );
    }

    @GetMapping("/funnel")
    @Operation(summary = "Получить воронку конверсии")
    public ResponseEntity<FunnelDto> getConversionFunnel(
            @Parameter(description = "Начало периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,

            @Parameter(description = "Конец периода")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,

            @Parameter(description = "ID события (опционально)")
            @RequestParam(required = false) Long eventId) {

        log.info("📊 GET /api/analytics/funnel");
        return ResponseEntity.ok(
                analyticsService.getConversionFunnel(start, end, eventId)
        );
    }
}