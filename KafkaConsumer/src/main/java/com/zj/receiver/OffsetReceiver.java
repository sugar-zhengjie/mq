package com.zj.receiver;

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 14:21
 */
@Component
public class OffsetReceiver {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @KafkaListener(topics = {"topic9"}, groupId = "group1", containerFactory = "manualKafkaListenerContainerFactory")
    public void manualCommit(@Payload String message,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             Consumer consumer, Acknowledgment ack) {
        log.info("手动提交偏移量 , partition={}, msg={}", partition, message);
        // 同步提交
        consumer.commitSync();
        //异步提交
        //consumer.commitAsync();

        // ack提交也可以，会按设置的ack策略走(参考MyOffsetConfig.java里的ack模式)
        // ack.acknowledge();
    }

    @KafkaListener(topics = {"topic9"}, groupId = "group2", containerFactory = "manualKafkaListenerContainerFactory")
    public void noCommit(@Payload String message,
                         @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         Consumer consumer, Acknowledgment ack) {
        log.info("忘记提交偏移量, partition={}, msg={}", partition, message);
        // 不做commit！
    }

    /**
     * 现实状况：
     * commitSync和commitAsync组合使用
     * 手工提交异步 consumer.commitAsync();
     * 手工同步提交 consumer.commitSync()
     * commitSync()方法提交最后一个偏移量。在成功提交或碰到无怯恢复的错误之前，
     * commitSync()会一直重试，但是commitAsync()不会。
     * 一般情况下，针对偶尔出现的提交失败，不进行重试不会有太大问题
     * 因为如果提交失败是因为临时问题导致的，那么后续的提交总会有成功的。
     * 但如果这是发生在关闭消费者或再均衡前的最后一次提交，就要确保能够提交成功。否则就会造成重复消费
     * 因此，在消费者关闭前一般会组合使用commitAsync()和commitSync()。
     */
    @KafkaListener(topics = "topic9", groupId = "group3",containerFactory = "manualKafkaListenerContainerFactory")
    public void manualOffset(@Payload String message,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             Consumer consumer,
                             Acknowledgment ack) {
        try {
            log.info("同步异步搭配 , partition={}, msg={}", partition, message);
            //先异步提交
            consumer.commitAsync();
            //继续做别的事
        } catch (Exception e) {
            System.out.println("commit failed");
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }

    }
}
