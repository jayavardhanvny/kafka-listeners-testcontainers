package com.example.kafka.demo.listener;

import com.example.kafka.demo.model.TestEvent;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestEventHandler {


    @KafkaListener(id = "TestEventHandler", topics = "kafka-listener", groupId = "demo-consumer"
            , containerFactory = "getConsumerFactory"
    )
    public void handle(ConsumerRecord<String, TestEvent>  event) {
        log.info(
                "Received a event with details {}: ", event.value()
        );
    }
}
