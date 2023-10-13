package com.zj.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/13 10:28
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "springboot-mq",
        consumerGroup = "springboot-mq-consumer-1"
)
//@RocketMQMessageListener(consumerGroup = "monitor",
//        topic = "order",
//        selectorType = SelectorType.TAG,
//        selectorExpression = "phone",
//        consumeMode = ConsumeMode.ORDERLY)

public class Consumer implements RocketMQListener<MessageExt> {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(MessageExt messageExt) {
        String msgId = messageExt.getMsgId();
        byte[] body = messageExt.getBody();
        String message = new String(body, StandardCharsets.UTF_8);

        // 幂等 通过维护一个记录已处理消息ID的Set集合（redis），如果存在则消费过，不消费
        if(!Optional.ofNullable(msgId).isPresent()){
            return;
        }

        log.info("Receive message："+message);
    }

//    @Override
//    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//        if (CollectionUtils.isEmpty(msgList)) {
//            log.info("MQ接收消息为空，直接返回成功");
//            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//        }
//        MessageExt messageExt = msgList.get(0);
//        log.info("MQ接收到的消息为：" + messageExt.toString());
//        try {
//            String topic = messageExt.getTopic();
//            String tags = messageExt.getTags();
//            String body = new String(messageExt.getBody(), "utf-8");
//
//            log.info("MQ消息topic={}, tags={}, 消息内容={}", topic, tags, body);
//        } catch (Exception e) {
//            log.error("获取MQ消息内容异常{}", e);
//        }
//        // 处理业务逻辑
//        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//    }

}

/*
    幂等
        1.设置唯一标识id
        2.设置消费进度存储策略，配置文件
        3.集群模式下设置不同的消费者组，同一消费者组，相同的topic和tag，只消费一部分消息，避免了重复消费的问题，并且能够实现消息的负载均衡和并行处理
 */

/*
    消息不丢失
        1.刷盘机制
        2.ACK 机制
        3.消息存储机制
 */

//        1.订单系统完成支付,扣减余额
//        try{
//          2.发送消息
//        }catch{
//          3.消息发送失败保存消息表
//        }

