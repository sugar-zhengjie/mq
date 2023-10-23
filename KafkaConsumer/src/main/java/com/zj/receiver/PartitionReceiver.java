package com.zj.receiver;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 13:23
 */
@Component
public class PartitionReceiver {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @KafkaListener(topics = {"topic4","topic5"},topicPattern = "0")
    public void onMessage0(ConsumerRecord<?,?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if(optional.isPresent()){
            Object msg = optional.get();
            System.out.println("partition=0,message:[" + msg + "]");
        }
    }

    @KafkaListener(topics = {"topic4","topic5"},topicPattern = "1")
    public void onMessage1(ConsumerRecord<?,?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if(optional.isPresent()){
            Object msg = optional.get();
            System.out.println("partition=1,message:[" + msg + "]");
        }
    }

    @KafkaListener(topics = {"topic4"},topicPattern = "2")
    public void onMessage2(ConsumerRecord<?,?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if(optional.isPresent()){
            Object msg = optional.get();
            System.out.println("partition=2,message:[" + msg + "]");
        }
    }

    // 指定topic、partition、offset消费
    // topics和topicPartitions不能同时使用
    /**
     * @Title 指定topic、partition、offset消费
     * @Description 同时监听topic1和topic2，监听topic1的0号分区、topic2的 "0号和1号" 分区，指向1号分区的offset初始值为8
     **/
    @KafkaListener(id = "consumer1", groupId = "group1",
            topicPartitions = {
            @TopicPartition(topic = "topic4", partitions = { "0" }),
            @TopicPartition(topic = "topic5", partitions = "0", partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "8"))
    })
    public void onMessage3(ConsumerRecord<?, ?> record) {
        System.out.println("topic:"+record.topic()+"|partition:"+record.partition()+"|offset:"+record.offset()+"|value:"+record.value());
    }
}
