package com.zj.receiver;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 10:47
 */
@Component
public class SimpleReceiver {

    @Autowired
    private KafkaTemplate<String,Object>  kafkaTemplate;

    @KafkaListener(topics = {"topic1","topic2","topic3"})
    public void onMessageSimple(ConsumerRecord<?,?> record){
        System.out.println("简单消费："+record.topic()+"-"+record.partition()+"-"+record.value());
    }
}
