package com.zj.base.simple;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhengjie
 */
@Component
public class Consumer {

    public void handler() throws Exception {
        // 1.创建消费者Consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        // 2.指定Nameserver地址
        consumer.setNamesrvAddr("ip1:port;ip2:port");
        // 3.订阅主题Topic和Tag  * 代表所有Tag
        consumer.subscribe("base","*");
        // 设定消费模式：负载均衡|广播模式 默认负载均衡
        consumer.setMessageModel(MessageModel.BROADCASTING);
        // 4.设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt msg : msgs) {
                    System.out.println("consumeThread=" + Thread.currentThread().getName() + "," + new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        // 5.启动消费者consumer
        consumer.start();
    }

}
