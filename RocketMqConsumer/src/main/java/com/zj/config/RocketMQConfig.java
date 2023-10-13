package com.zj.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/13 15:38
 */
@Configuration
public class RocketMQConfig {
    @Value("${rocketmq.consumer.group-name}")
    private String groupName;

    @Value("${rocketmq.consumer.consume-message-batch-max-size}")
    private int consumeMessageBatchMaxSize;

    @Value("${rocketmq.consumer.persist-consumer-offset-interval}")
    private int persistConsumerOffsetInterval;

    @Bean
    public DefaultMQPushConsumer consumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        consumer.setPersistConsumerOffsetInterval(persistConsumerOffsetInterval);
        return consumer;
    }
}
