package com.zj.receiver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 14:16
 */
@Component
public class TranspondReceiver {

    //使用@SendTo进行转发

    @KafkaListener(topics = {"topic7"})
    @SendTo("topic8")
    public String onMessage7(ConsumerRecord<?, ?> record) {
        return record.value()+"-forward message";
    }

    @KafkaListener(topics = {"topic8"})
    public void onMessage8(ConsumerRecord<?, ?> record) {
        System.out.println(record.value());
    }
}
