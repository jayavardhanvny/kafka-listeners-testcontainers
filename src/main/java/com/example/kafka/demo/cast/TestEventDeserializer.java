package com.example.kafka.demo.cast;

import com.example.kafka.demo.model.TestEvent;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class TestEventDeserializer extends JsonDeserializer {
    private final JsonDeserializer<TestEvent> deserializer;

    public TestEventDeserializer() {
        this.deserializer = new JsonDeserializer<>(TestEvent.class);
        deserializer.addTrustedPackages("*");
    }

    @Override
    public TestEvent deserialize(String topic, Headers headers, byte[] data) {
        return deserializer.deserialize(topic, headers, data);
    }
}
