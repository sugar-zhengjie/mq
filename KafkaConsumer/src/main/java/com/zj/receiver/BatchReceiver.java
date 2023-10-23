package com.zj.receiver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 13:49
 */
@Component
public class BatchReceiver {

    //需要修改配置文件支持批量

    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
        return (message, exception, consumer) -> {
            System.out.println("消费异常："+message.getPayload());
            return null;
        };
    }


    @KafkaListener(id = "consumer1",groupId = "group1", topics = "topic6",errorHandler = "consumerAwareErrorHandler")
    public void onMessage3(List<ConsumerRecord<?, ?>> records) {
        System.out.println(">>>批量消费一次，records.size()="+records.size());
        for (ConsumerRecord<?, ?> record : records) {
            System.out.println(record.value());
        }
    }

}
