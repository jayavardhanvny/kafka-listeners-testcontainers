package com.example.kafka.demo.kafka;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import com.example.kafka.demo.model.TestEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@SpringBootTest
@TestPropertySource(
        properties = {
                "spring.kafka.consumer.auto-offset-reset=earliest",
        }
)
@Testcontainers
@TestConfiguration
public class TestEventHandlerTest {

    String topicName = "kafka-listener";

//    @Spy
//    ProductPriceChangedEventHandler productPriceChangedEventHandler;

    @Container
    public static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
    );

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldHandleProductPriceChangedEvent() {
        TestEvent event = new TestEvent(1, "vinay");
        log.info("logger 1");
        kafkaTemplate.send(topicName, "12", event);

        await()
                .pollInterval(Duration.ofSeconds(10))
                .atMost(20, SECONDS)
                .untilAsserted(() -> {
                    log.info("logger 2");

//                    ArgumentCaptor<ConsumerRecord> messageCaptor = ArgumentCaptor.forClass(ConsumerRecord.class);
//                    Mockito.verify(productPriceChangedEventHandler, timeout(5000)).handle(messageCaptor.capture());
//
//                    assertEquals(event, messageCaptor.getValue());
//                    ConsumerRecord<String, TestEvent> singleRecord =
//                            KafkaTestUtils.getSingleRecord(getConsumerFactory, topicName);
//                    assertEquals(event, singleRecord.value());
                });
    }
}
