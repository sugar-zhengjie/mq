package com.zj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 14:08
 */

@RestController
public class OffsetController {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void send(){
        String topic = "topic9";
        for (int i = 0; i < 10; i++) {
            String message = "Message " + i;
            kafkaTemplate.send(topic,message);
        }
    }
}
