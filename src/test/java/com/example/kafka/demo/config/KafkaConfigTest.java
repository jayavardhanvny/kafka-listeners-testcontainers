package com.example.kafka.demo.config;

import com.example.kafka.demo.cast.TestEventDeserializer;
import com.example.kafka.demo.kafka.TestEventHandlerTest;
import com.example.kafka.demo.model.TestEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.HashMap;

@Configuration
@Profile("test")
public class KafkaConfigTest {


    @Bean
    ConsumerFactory<String, TestEvent> getDefaultConsumer() {
        StringDeserializer stringDeserializer = new StringDeserializer();
        TestEventDeserializer testEventDeserializer = new TestEventDeserializer();

        DefaultKafkaConsumerFactory factory = new DefaultKafkaConsumerFactory<>(consumerConfig(), new ErrorHandlingDeserializer<>(stringDeserializer),
                new ErrorHandlingDeserializer<>(testEventDeserializer));
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, TestEvent>> getConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TestEvent> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(getDefaultConsumer());
        return concurrentKafkaListenerContainerFactory;
    }

    private Map<String, Object> consumerConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, TestEventHandlerTest.kafka.getBootstrapServers());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer");
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return configProps;

    }
}
