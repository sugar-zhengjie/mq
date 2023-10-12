package com.zj.base.simple;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhengjie
 */
@Component
public class OneWayProducer {

    public void handler() throws Exception {
        //1.创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("192.168.25.135:9876;192.168.25.138:9876");
        //3.启动producer
        producer.start();

        for (int i = 0; i < 3; i++) {
            //4.创建消息对象，指定主题Topic、Tag和消息体
            /*
              参数一：消息主题Topic
              参数二：消息Tag
              参数三：消息内容
             */
            Message msg = new Message("simple", "oneway", ("Hello World，单向消息" + i).getBytes());
            //5.发送单向消息
            producer.sendOneway(msg);

            //线程睡1秒
            TimeUnit.SECONDS.sleep(5);
        }

        //6.关闭生产者producer
        producer.shutdown();
    }
}
