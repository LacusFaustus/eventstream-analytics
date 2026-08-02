package ru.faustus.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneratorApplication.class, args);
        System.out.println("========================================");
        System.out.println("  📊 DATA GENERATOR STARTED");
        System.out.println("  🔥 Generating up to 5000 events/sec");
        System.out.println("  📤 Sending to Kafka: events.raw.v1");
        System.out.println("========================================");
    }
}