package com.zj.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 13:35
 */
@Configuration
public class PartitionTemplate {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    KafkaTemplate<String,String> kafkaTemplate;

    @PostConstruct
    public void setKafkaTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //注意分区器在这里！！！
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomizePartitioner.class);
        this.kafkaTemplate = new KafkaTemplate<String, String>(new DefaultKafkaProducerFactory<>(props));
    }

    public KafkaTemplate<String,String> getKafkaTemplate(){
        return kafkaTemplate;
    }

}
