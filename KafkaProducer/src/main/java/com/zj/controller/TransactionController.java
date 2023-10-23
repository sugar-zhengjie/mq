package com.zj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 15:52
 */
@RestController
public class TransactionController {
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void sendBatchMessages(List<String> messages) {
        kafkaTemplate.executeInTransaction(kafkaOperations -> {
            String topic = "topic10";
            for (int i = 0; i < 10; i++) {
                kafkaTemplate.send(topic,"msg"+i).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
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
                //模拟一个发送出错
                if(i == 6){
                    throw new RuntimeException("exception!");
                }
            }
            return null;
        });
    }
}
