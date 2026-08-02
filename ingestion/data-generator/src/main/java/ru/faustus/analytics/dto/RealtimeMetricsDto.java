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
public class RealtimeMetricsDto {
    private Long activeUsers;
    private Long eventsPerSecond;
    private List<Map<String, Object>> topEvents;
    private Double systemLoad;
}