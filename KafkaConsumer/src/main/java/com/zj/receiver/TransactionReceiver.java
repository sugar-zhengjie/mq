package com.zj.receiver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 15:37
 */
@Component
public class TransactionReceiver {

    @KafkaListener(topics = {"topic10"})
    @SendTo("topic11")
    public void onMessage7(List<ConsumerRecord<?, ?>> records) {
        System.out.println(">>>批量消费一次，records.size()="+records.size());
        for (ConsumerRecord<?, ?> record : records) {
            System.out.println(record.value());
        }
    }

    @KafkaListener(topics = {"topic11"})
    public void onMessage8(ConsumerRecord<?, ?> record) {
        System.out.println("ListenTransactionForward Received: "+record.value());
    }


}
