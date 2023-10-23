package com.zj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 10:33
 */
@RestController
public class SimpleController {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    // KafkaTemplate调用send时默认采用异步发送，如果需要同步获取发送结果，调用get方法

    public void sendSimple(){
        kafkaTemplate.send("topic1","123");
    }

    // kafkaTemplate提供了一个回调方法addCallback，我们可以在回调方法中监控消息是否发送成功 或 失败时做补偿处理

    public void addCallback1Send(){
        kafkaTemplate.send("topic2","2222").addCallback(success -> {
            if (success != null) {
                // topic
                String topic = success.getRecordMetadata().topic();
                // 分区
                int partition = success.getRecordMetadata().partition();
                // 分区内的偏移位置
                long offset = success.getRecordMetadata().offset();
                System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);
            }
        },failure -> {
            System.out.println("发送消息失败:" + failure.getMessage());
        });
    }

    public void addCallback2Send(){
        kafkaTemplate.send("topic3","3333").addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("发送消息失败："+throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("发送消息成功：" + result.getRecordMetadata().topic() + "-"
                        + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
            }
        });
    }
}
