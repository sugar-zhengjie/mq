package com.zj.controller;

import com.zj.config.PartitionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 13:15
 */
@RestController
public class PartitionController {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    //指定分区发送，不管key是什么，到同一个分区 第二个参数指定了分区
    public void samePartitionSend(){
        kafkaTemplate.send("topic4", 0, "消息的key：11", "11消息的value：自己写入");
        kafkaTemplate.send("topic4", 0, "消息的key：22", "22消息的value：自己写入");
    }

    //指定key发送，不指定分区，根据key做hash，相同key到同一个分区
    public void sendPartitionByKey(){
        kafkaTemplate.send("topic4", "key1", "value1");
        kafkaTemplate.send("topic4", "key2", "value2");
        kafkaTemplate.send("topic4", "key3", "value3");
        kafkaTemplate.send("topic4", "key4", "value4");
    }

    // 自定义分区模板发送

    @Autowired
    private PartitionTemplate partitionTemplate;

    public void customizePartitionSend(){
        partitionTemplate.getKafkaTemplate().send("topic5","111","key:111,msg:自定义分区策略");
        partitionTemplate.getKafkaTemplate().send("topic5","000","key:000,msg:自定义分区策略");
        partitionTemplate.getKafkaTemplate().send("topic5","123","key:123,msg:自定义分区策略");
    }
}

