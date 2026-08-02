package ru.faustus.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventGenerator {

    private final KafkaProducer<String, String> kafkaProducer;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.events-raw:events.raw.v1}")
    private String topic;

    @Value("${generator.events-per-second:1000}")
    private int eventsPerSecond;

    private final Random random = new Random();
    private long counter = 0;

    private static final List<String> CATEGORIES = Arrays.asList(
            "Concert", "Festival", "Exhibition", "Workshop",
            "Conference", "Sport", "Theatre", "Cinema"
    );

    private static final List<String> EVENT_TYPES = Arrays.asList(
            "VIEW", "REGISTER", "LIKE", "COMMENT", "SHARE", "PURCHASE"
    );

    private static final List<String> CITIES = Arrays.asList(
            "Moscow", "Saint Petersburg", "Novosibirsk", "Yekaterinburg",
            "Kazan", "Nizhny Novgorod", "Chelyabinsk", "Omsk"
    );

    @Scheduled(fixedRate = 100) // 10 раз в секунду
    public void generateEvents() {
        int batchSize = eventsPerSecond / 10;

        for (int i = 0; i < batchSize; i++) {
            try {
                Map<String, Object> event = generateEvent();
                String json = objectMapper.writeValueAsString(event);

                ProducerRecord<String, String> record = new ProducerRecord<>(
                        topic,
                        String.valueOf(event.get("userId")),
                        json
                );

                kafkaProducer.send(record);
                counter++;
            } catch (Exception e) {
                log.error("Error sending event", e);
            }
        }

        if (counter % 1000 == 0) {
            log.info("📊 Generated {} events", counter);
        }
    }

    private Map<String, Object> generateEvent() {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("eventId", UUID.randomUUID().toString());
        event.put("userId", random.nextLong(1, 100_000));
        event.put("eventType", EVENT_TYPES.get(random.nextInt(EVENT_TYPES.size())));
        event.put("category", CATEGORIES.get(random.nextInt(CATEGORIES.size())));
        event.put("city", CITIES.get(random.nextInt(CITIES.size())));
        event.put("device", random.nextBoolean() ? "MOBILE" : "DESKTOP");
        event.put("os", random.nextBoolean() ? "iOS" : "Android");
        event.put("sessionId", UUID.randomUUID().toString());
        event.put("timestamp", Instant.now().toEpochMilli());
        event.put("lat", random.nextDouble(-90, 90));
        event.put("lon", random.nextDouble(-180, 180));
        return event;
    }
}