package com.zj.base.simple;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhengjie
 * 发送异步消息
 */
@Component
public class AsyncProducer {

    public void handler() throws Exception {
        // 1.创建生产者，并指定组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        // 2.指定nameserver地址
        producer.setNamesrvAddr("ip1:port;ip2:port");
        // 3.启动producer
        producer.start();

        for(int i = 0; i < 10; i ++){
            // 4.创建消息对象，指定主题Topic、Tag和消息体
            /*
              参数一：消息主题Topic
              参数二：消息Tag
              参数三：消息内容
             */
            Message msg =  new Message("simple","tag_base_asyn",("Hello RocketMq" + i).getBytes());
            // 5.发送异步消息
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送结果：" + sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("发送异常：" + throwable);
                }
            });

            //线程睡1秒
            TimeUnit.SECONDS.sleep(1);
        }

        // 6.关闭生产者producer
        producer.shutdown();
    }
}
