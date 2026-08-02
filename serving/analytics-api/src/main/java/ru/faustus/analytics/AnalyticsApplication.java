package ru.faustus.analytics;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║  📊 ANALYTICS API — SPRING BOOT APPLICATION                ║
 * ║  REST API для бизнес-аналитики и дашбордов                ║
 * ║  Порт: 8088                                               ║
 * ╚══════════════════════════════════════════════════════════════╝
 *
 * Возможности API:
 * 1️⃣ Дашборд с ключевыми метриками (DAU, MAU, Retention)
 * 2️⃣ Real-time метрики (активные пользователи, события/сек)
 * 3️⃣ Детальная аналитика по событиям и пользователям
 * 4️⃣ Тренды и воронка конверсии
 * 5️⃣ Кэширование через Redis для быстрых ответов
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@OpenAPIDefinition(
        info = @Info(
                title = "EventStream Analytics API",
                version = "1.0.0",
                description = "API для бизнес-аналитики и дашбордов"
        )
)
public class AnalyticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnalyticsApplication.class, args);
        System.out.println("========================================");
        System.out.println("  📊 ANALYTICS API STARTED");
        System.out.println("  📍 Swagger UI: http://localhost:8088/swagger-ui.html");
        System.out.println("  📍 Health:     http://localhost:8088/actuator/health");
        System.out.println("  📍 Dashboard:  http://localhost:8088/api/analytics/dashboard");
        System.out.println("========================================");
    }
}