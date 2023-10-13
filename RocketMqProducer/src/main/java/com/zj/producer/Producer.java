package com.zj.producer;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/13 10:29
 */
@Component
public class Producer {

    @Autowired
    private RocketMQTemplate rocketMqTemplate;

    // 同步
    public void handler(){
        rocketMqTemplate.convertAndSend("springboot-mq","hello springboot rocketmq");
    }

    // 异步
    public void handler2(){
        SendCallback sendCallback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功！--->"+sendResult.getSendStatus());
            }
            @Override
            public void onException(Throwable e) {
                System.out.println("发送失败！");
            }
        };
        rocketMqTemplate.asyncSend("", "", sendCallback );
    }

    // 顺序
    public void handler3(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Message<String> message1 = MessageBuilder.withPayload(uuid).build();
        //order:phone 其中order是topic ，phone是tag
        rocketMqTemplate.syncSendOrderly("order:phone",message1,uuid);
    }

    // 延时
    public void handler4(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Message<String> message1 = MessageBuilder.withPayload(uuid).build();
        rocketMqTemplate.syncSend("order:phone",message1,30*1000,3);
    }

    // 事务 配置事务处理
    public void handler5(){
        Map map = new HashMap();
        Message<?> mes = MessageBuilder.withPayload(map).build();
        TransactionSendResult sendMessageInTransaction = rocketMqTemplate.sendMessageInTransaction("topic", mes , map );
        LocalTransactionState localTransactionState = sendMessageInTransaction.getLocalTransactionState();
        if (!"ROLLBACK_MESSAGE".equals(localTransactionState)) {
            // 回滚业务代码
        };
        System.out.println(localTransactionState);
    }

}
