package com.example.kafka.demo.config;

import com.example.kafka.demo.cast.TestEventDeserializer;
import com.example.kafka.demo.model.TestEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("!test")
public class KafkaConfig {

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, TestEvent>> getConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TestEvent> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(getDefaultConsumer());
        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    public ConsumerFactory<String, TestEvent> getDefaultConsumer() {
        StringDeserializer stringDeserializer = new StringDeserializer();
        TestEventDeserializer testEventDeserializer = new TestEventDeserializer();

        DefaultKafkaConsumerFactory factory = new DefaultKafkaConsumerFactory<>(consumerConfig(), new ErrorHandlingDeserializer<>(stringDeserializer),
                new ErrorHandlingDeserializer<>(testEventDeserializer));
        return factory;
    }

    private Map<String, Object> consumerConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer");
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return configProps;

    }

}
