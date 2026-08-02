package ru.faustus.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO для воронки конверсии.
 * Шаги: VIEW → REGISTER → LIKE → COMMENT → PURCHASE
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunnelDto {
    private List<String> steps;
    private List<Long> counts;
    private List<Double> conversionRates;
    private List<Double> dropOffRates;
    private Long totalUsers;
    private Double overallConversionRate;
}